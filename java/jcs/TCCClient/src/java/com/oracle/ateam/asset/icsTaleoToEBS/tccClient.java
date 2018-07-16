/**
 *  Copyright © 2018, Oracle and/or its affiliates. All rights reserved.
 *  The Universal Permissive License (UPL), Version 1.0
 *    **/
package com.oracle.ateam.asset.icsTaleoToEBS;

import com.taleo.integration.client.Client;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author 
 */
public class tccClient {

    public String runTCCJoB(String strJobLocation) {
        Process p=null;
        try {
            System.out.println("Launching Taleo client. Path:" + strJobLocation);
            String cmd = "{TCC Installation path on server}/scripts/client.sh " + strJobLocation;
            p = Runtime.getRuntime().exec(cmd);
            ReadStream s1 = new ReadStream("stdin", p.getInputStream());
            ReadStream s2 = new ReadStream("stderr", p.getErrorStream());
            s1.start();
            s2.start();
            p.waitFor();
            return "SUCCESS";
        } catch (Exception e) {
             System.out.println("runTCCJob Error:" + e.getMessage());
             return "FAILURE";
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
    }

}
