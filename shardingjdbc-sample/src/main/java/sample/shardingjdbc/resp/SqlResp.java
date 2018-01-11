package sample.shardingjdbc.resp;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by za-daixiaojun on 2018/1/9.
 */
@Getter
@Setter
public class SqlResp {
    private List<String> columnNames = new ArrayList<>();
    private List<List<String>> rows = new ArrayList<>();

    private List<Integer> effectRows = new ArrayList<>();
    private boolean success = true;
    private List<String> executeSqlDetails = new ArrayList<>();

    public void printf() {
        for (String columnName : columnNames) {
            System.out.printf("%20s", columnName);
        }
        System.out.println();

        for (List<String> row : rows) {
            for (String column : row) {
                System.out.printf("%20s", column);
            }
            System.out.println();
        }
    }
}
