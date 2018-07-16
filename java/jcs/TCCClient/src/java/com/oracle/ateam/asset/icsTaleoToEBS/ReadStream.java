/**
 *  Copyright © 2018, Oracle and/or its affiliates. All rights reserved.
 *  The Universal Permissive License (UPL), Version 1.0
 *
 * **/
package com.oracle.ateam.asset.icsTaleoToEBS;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author 
 */
public class ReadStream implements Runnable {
    String name;
    InputStream is;
    Thread thread;      
    public ReadStream(String name, InputStream is) {
        this.name = name;
        this.is = is;
    }       
    public void start () {
        thread = new Thread (this);
        thread.start ();
    }       
    public void run () {
        try {
            InputStreamReader isr = new InputStreamReader (is);
            BufferedReader br = new BufferedReader (isr);   
            while (true) {
                String s = br.readLine ();
                if (s == null) break;
                System.out.println ("[" + name + "] " + s);
            }
            is.close ();    
        } catch (Exception ex) {
            System.out.println ("Problem reading stream " + name + "... :" + ex.getMessage());
        }
    }
}
