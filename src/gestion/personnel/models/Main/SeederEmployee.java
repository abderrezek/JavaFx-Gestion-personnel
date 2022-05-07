package gestion.personnel.models.Main;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public final class SeederEmployee {

    public static void run(int nbRows) {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        List<String> sqlsList = GetSQLs(nbRows);
        employeeDAO.DeleteAll();
        if (employeeDAO.InsertMultiple(sqlsList)) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }

    private static List<String> GetSQLs(int nbRows) {
        List<String> sqlsList = new ArrayList<>();
        String sql;
        String genre, date;
        GregorianCalendar gc = new GregorianCalendar();
        int year, dayOfYear;
        Random rand = new Random();
        for(int i = 1; i <= nbRows; i++) {
            year = randBetween(1980, 2021);
            gc.set(gc.YEAR, year);
            dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
            gc.set(gc.DAY_OF_YEAR, dayOfYear);
            date = gc.get(gc.YEAR) + "-" + (gc.get(gc.MONTH) + 1) + "-" + gc.get(gc.DAY_OF_MONTH);
            
            genre = rand.nextBoolean() ? Genre.Masculin.name() : Genre.Femelle.name();
            sql = "INSERT INTO employee (nom, prenom, date_naissance, age, lieu_naissance, adresse, wilaya, genre) "
                    + "VALUES ('n"+i+"', 'p"+i+"', '"+date+"', 12, 'l"+i+"', 'a"+i+"', 'w"+i+"', '"+genre+"');";
            sqlsList.add(sql);
        }
        return sqlsList;
    }

    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }
}