<!--- Copyright (c) 2017, Oracle and/or its affiliates. All rights reserved --->

# Taleo / EBS Integration via ICS

## Source Location

## Products Involved

* Oracle Integration Cloud Service
* Oracle Java Cloud Service
* Oracle Taleo Enterprise Edition
* Oracle eBusiness Suite 12.2.x

## Demonstrates

This solution demonstrates integrating Taleo Enterprise Edition and EBS using Integration Cloud Service (ICS). Taleo Connect Client (TCC)  scripts are exposed through a web service endpoint hosted on JCS so that ICS can orchestrate integration between Taleo Enterprise and EBS.

## Functional Overview

The roles of Taleo and EBS are defined as follows here: 

Recruitment processing, candidate filtering and background checks are done with Taleo. 

HCM backbone is EBS, it manages and maintains employee records, payments, career development etc

Once candidates are likely to become a hired employee, a record will be generated in EBS. The interface will return the EBS employee party id for cross referencing purposes. 

## Disclaimer

This solution was developed in order to allow customers to integrate Taleo Enterprise Edition and EBS using ICS, until an Integration adapter is available for Taleo Enterprise Edition. Please verify whether an integration adapter for Taleo Enterprise Edition is available before considering this solution.



## Implementation Scenarios provided 


### Candidate creation in EBS

This scenario runs like follows:

1. Taleo Connect Client (TCC) and SOAP Wrapper must be installed either on one of the following :
  * Own hosted server connected to the internet with WLS installed on Linux 
  * On a PaaS instance like JCS
  * On an IaaS host with WLS installed on Linux 
2. The Scheduler in ICS is configured to runs frequently in order to activate TCC so that it extracts new candidate records from Taleo
3. These records are then exported to a file stored in a staging folder on either the same host or alternately on Storage Cloud Service (or similar)
4. The ICS File Adapter reads the file, record by record, and invokes an ICS sub-flow which in turn invokes an EBS service
5. The EBS service creates a new candidate record in EBS. This candidate becomes a new HR Party ID assigned once successfully created. This Party ID will be the unique ID for this new employee once hired. 
6. The EBS sub-flow returns the Party ID, Start Date (usually system date) and End Date (here 4712-12-31 for "eternity" or "unset")
7. ICS File Adapter then writes the same input record back into Taleo , via a Taleo import file,  in another staging folder and enriches the data with the EBS Party ID (stored in field Comment)
8. Post processing of all records is then done by calling a SOAP service,  which in turn calls TCC for importing the enriched data  into Taleo Enterprise Edition.


## Content of this code repository

The code repository contains two sub-directories holding the ICS exports and Java code for this asset.

| **Directory**                | **Description**
| -------------------------------------| ----------------------------
| **/ics**            | all ICS exports  
| **/ics/single**      | ICS flow to call EBS sub-flow and EBS sub-flow itself
| **/java**      | Java code plus Shell Script for EBS Concurrent Manager to call Java  
| **/java/ics**       | Java code for SOAP wrapper to run TCC


## Running the integration

Import the flows into ICS. Connections to TCC and EBS are created in draft state. Modify connections as necessary and test them so that the integrations can be activated. Refer to latest documentation for ICS for detailed intructions on configuring and testing connections.

Compile the TCC SOAP Wrapper code and deploy to JCS. Verify that the SOAP endpoint is accessible using tool such as SOAPUi.

Following functional requirements in EBS must being configured and known by the integrator for a correct session initialization in the RESTHeader before doing the REST call:
* Responsibility - an existing EBS responsibility with all required permisions to acces the organization and related data objects
* RespApplication - the abbreviation of the EBS module providing this REST API - in this sample it will be 'PER'
* SecurityGroup - if not configured differently you can use the default value 'STANDARD'
* NLSLanguage - if not configured differently you must use the default value 'AMERICAN'
* Language - if not configured differently you must use the default value 'American' (notice the mixed case notation!)
* Org_Id - the numeric value of the EBS Organization related to this call (in this sample the HR Organization for the Candidate)

IRC_PARTY_API (PLSQL) REST service is required to be generated and deployed in EBS Integrated SOA Gateway. Refer to EBS documentation for instructions.

Make sure that candidate records are available in Taleo enterprise and excute the integrations. After execution, candidate records will have EBS employee number in comments section.



##Further Information

For further information on how this integration works and further details on how to run it in your own environment please see the following article on our new Solutions Documentation  area  https://docs.oracle.com/en/solutions/e-business-suite-and-taleo-with-integration-cloud/index.html 