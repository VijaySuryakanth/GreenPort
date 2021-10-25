/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.oci.processdata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author VKOKATNU
 */
public class ProcessMSExcel {

    public void service() {
        FileInputStream file = null;
        try {
            file = new FileInputStream(new File(""));
            Workbook workbook = new XSSFWorkbook(file);
        } catch (Exception ex) {
            Logger.getLogger(ProcessMSExcel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                file.close();
            } catch (IOException ex) {
                Logger.getLogger(ProcessMSExcel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
