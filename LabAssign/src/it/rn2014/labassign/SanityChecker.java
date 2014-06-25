package it.rn2014.labassign;

import java.sql.SQLException;
import java.sql.ResultSet;

public class SanityChecker {

    MySqlConnector mc;

    SanityChecker(MySqlConnector m) {
        mc = m;
    }

    boolean checkIsZero(String query, String desc){
        ResultSet results = null;
        int qres = -1;
        System.out.print("Running check: " + desc + ".. ");
       try {
            results = mc.executeRaw(query);
            qres = Integer.parseInt(results.getString(0));
       } catch (SQLException e){ e.printStackTrace();}
       if(results != null){
        if (qres == 0){
            System.out.println("\tOK");
            return true;
        }
       }
       System.out.println("FAILED");
       return false;
    }

    // Ogni evento organizzato da RS deve avere come idgruppo ed idunita una coppia gruppo unità valido
    String GroupAndUnit = ""; /* tod: to be implemented */    

    String Non3StradeBoys = "SELECT COUNT( * )" +
                       "FROM  `ragazzi`" +
                       "WHERE stradadicoraggio1 + stradadicoraggio2 + " +
                       "stradadicoraggio3 + stradadicoraggio4 + stradadicoraggio5 !=3";

    /* Questo è assicurato dal vincolo non null del campo nella tabella */
    String MinMaxAgeSet = "";

    String MaxMinAge = "SELECT COUNT(id) FROM `laboratori` WHERE etamassima < etaminima;";
    
    String StradeEvents = "SELECT COUNT( * )" +
                        "FROM  `tavolerotonde`" +
                        "WHERE stradacoraggio1 + stradacoraggio2 + stradacoraggio3 + stradacoraggio4 + stradacoraggio5 !=1";

    String StradeEvents2 = "SELECT COUNT( * ) " +
                        "FROM  `laboratori` "+
                        "WHERE stradadicoraggio1 + stradadicoraggio2 + " +
                        "stradadicoraggio3 + stradadicoraggio4 + stradadicoraggio5 !=1";

    String TotalNumber1 = "SELECT COUNT(*) from `ragazzi`;";
    String TotalNumber2 = "SELECT SUM(maxpartecipanti) FROM `laboratori` WHERE 1";
    String TotalNumber3 = "SELECT SUM(maxpartecipanti) FROM `tavolerotonde` WHERE 1";

    String NTurns = "SELECT COUNT(*) FROM `tavolerotonde` "+
                        "WHERE turno1 + turno2 + turno3 !=1";

    //  I vincoli preimpostati devono essere relativi a ragazzi presenti nel DB
    // I vincoli preimpostati devono essere relativi ad eventi con il codice presenti nel DB
    String Constraints = ""; /* todo: to be impleted*/

    // Ogni rover deve avere un gruppo associato che sia presente nella tabella gruppi
    String ValidGroup = ""; /* todo: to be impleted*/
    
    void runAllChecks () {

        checkIsZero(NTurns, "Corretto numero di turni");
        checkIsZero(StradeEvents, "Unica strada per laboratori");
        checkIsZero(StradeEvents2, "Unica strada per tavole rotonde");
        checkIsZero(MaxMinAge, "Età massima maggiore della minima");
        checkIsZero(Non3StradeBoys, "3 strade per ogni ragazzo");
        
    }
}
