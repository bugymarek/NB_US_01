/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.util.ArrayList;

/**
 *
 * @author Bugy
 */
public class ManagerHTML {

    public ManagerHTML() {
    }
    
    public String createTable(String headerHtml, String rowsHtml) {
        return "<table style=\"width:100%\">" + headerHtml + rowsHtml + "</table>";
    }
    
    public String createTableHeader(String[] headerArr) {
        String result = "<tr>";
        for (String s : headerArr) {
            result += "<th>";
            result += s;
            result += "</th>";
        }
        result += "</tr>";
      return result;
    }
    
    public String createTableRow(ArrayList<String> data) {
        String result = "<tr>";
        for (String s : data) {
            result += "<td>";
            result += s;
            result += "</td>";
        }
        result += "</tr>";
      return result;
    }
}
