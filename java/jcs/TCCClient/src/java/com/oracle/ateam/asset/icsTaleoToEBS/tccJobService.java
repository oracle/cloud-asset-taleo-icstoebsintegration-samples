/**
 *  Copyright © 2018, Oracle and/or its affiliates. All rights reserved.
 *  The Universal Permissive License (UPL), Version 1.0
 *   **/

package com.oracle.ateam.asset.icsTaleoToEBS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author 
 */
@WebService(serviceName = "tccJobService")
public class tccJobService {

    /**
     * Run TCC client with configuration file provided in JobPath. Depending on
     * type of the job, the job produces a files or reads a file for import. For
     * Export jobs, upon successful completion, the file has to be retrieved,
     * possibly by FTP or by other means For import jobs, before start of job,
     * the file has to be placed at predetermined location with pre-determined
     * name.
     *
     * @param JobPath
     * @return String with results of execution
     */
    @WebMethod(operationName = "runTCCJob")
    public String runTCCJob(@WebParam(name = "JobPath") String JobPath) {
        try {
            //tccClient().runTCCJob(JobPath);
            return new tccClient().runTCCJoB(JobPath);
        } catch (Exception ex) {
            System.out.println("runTCCJob Error:" + ex.getMessage());
            return "FAILED:" + ex.getMessage();
        }
    }

    /**
     * Run TCC client with export configuration file provided in JobPath. Then,
     * get contents of file provided in FilePath
     *
     * @param JobPath
     * @param FilePath
     * @return String with results of execution
     */
    @WebMethod(operationName = "runTCCExportJob")
    public TCCResponse runTCCExportJob(@WebParam(name = "JobPath") String JobPath, @WebParam(name = "FilePath") String FilePath) {
        try {
            TCCResponse objTCCResponse = new TCCResponse();
            objTCCResponse.setStrTCCReponse(new tccClient().runTCCJoB(JobPath));

            //If the job succeeded, Read file and convert to base64
            if (objTCCResponse.getStrTCCReponse().contains("FAILED")) {
                objTCCResponse.setStrFileBase64("");
            } else {
                File originalFile = new File(FilePath);
                String encodedBase64 = null;
                FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
                byte[] bytes = new byte[(int) originalFile.length()];
                fileInputStreamReader.read(bytes);
                encodedBase64 = Base64.getEncoder().encodeToString(bytes);

                objTCCResponse.setStrFileBase64(encodedBase64);
            }
            return objTCCResponse;
        } catch (Exception ex) {
            System.out.println("runTCCExportJob Error:" + ex.getMessage());
            TCCResponse objTCCResponse = new TCCResponse();
            objTCCResponse.setStrTCCReponse("FAILED:" + ex.getMessage());
            objTCCResponse.setStrFileBase64("");
            return objTCCResponse;
        }
    }

    /**
     * Write content of Base64 input to file specified by FilePath. Then, run
     * TCC client with import configuration file provided in JobPath.
     *
     * @param JobPath
     * @param FilePath
     * @param Base64Content
     * @return String with results of execution
     */
    @WebMethod(operationName = "runTCCImportJob")
    public String runTCCImportJob(@WebParam(name = "JobPath") String JobPath, @WebParam(name = "FilePath") String FilePath, @WebParam(name = "Base64Content") String Base64Content) {
        try {

            //Write file to the path     
            byte[] data = Base64.getDecoder().decode(Base64Content.replaceAll("\n", ""));
            OutputStream stream = new FileOutputStream(FilePath);
            stream.write(data);

            //Launch the client
            return new tccClient().runTCCJoB(JobPath);
        } catch (Exception ex) {
            System.out.println("runTCCImportJob Error:" + ex.getMessage());
            return "FAILED:" + ex.getMessage();
        }
    }

}
