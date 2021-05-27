import os
import subprocess
import threading
import re
import time
import json

from datetime import datetime
from pathlib import Path
from random import randrange, randint
from statistics import mean

from elasticsearch import Elasticsearch
from elasticsearch_dsl import Search

class runner (threading.Thread):
    def __init__(self, name, method):
        threading.Thread.__init__(self)
        self.name = name
        self.method = method
        
    def run(self):
        print("Starting Thread " + self.name)
        executeJarRunner(self.method)
        print("Exiting Thread "+ self.name)

def parseException(file):
    with open(file) as f:
        text = f.read()
    exceptions = re.findall(r'(?m)^.*?Exception.*(?:\n+^\s*at .*)+', text, re.M)
    # for exception in exceptions:
    #     print(exception)
    
    exceptions[:] = [e for e in exceptions if "MTPException" not in e]
    exceptions[:] = [e for e in exceptions if "InterruptedException" not in e]
        
    # for num, e in enumerate(exceptions):
    #     if any("MTPException" in e for e in exceptions):
    #         exceptions.pop(num)
    #     elif any("InterruptedException" in e for e in exceptions):
    #         exceptions.pop(num)
    return exceptions   

def getJarFile():
    base = Path(__file__).parent.parent / "build/libs"
    for file in os.listdir(base):
        if "all" in file:
            print(base / file)
            return base / file

def executeJarRunner(mr):
    ret, dt_string, ipaddr, fname, var_dict = executeJar(mr)
    if ("MRConsistentEmergencyResponse" in mr):
        checkMRConsistentEmergencyResponse(index=dt_string, host=ipaddr, fname=fname, var_dict=var_dict, ret=ret)

        
def randomiseVariables(size="r.small"):
    #1 sh smarthome = 1000 units
    #control scenarios
    if ("r.small" in size):
        #small cities
        argument_dict = {
            "a" : randint(1,20),
            "ff" : randint(1,20),
            "p" : randint(1,50),
            "h" : 4,
            "bh" : 4
        }
        
    elif ("r.original" in size):
        #small cities
        argument_dict = {
            "a" : randint(1,100),
            "ff" : randint(1,100),
            "p" : randint(1,100),
            "h" : randint(1,100),
            "bh" : randint(1,100),
        }
    
    
    # argument_list.append(pg,pd,sh,ev)
    print(argument_dict)
    return argument_dict

def executeJar(mr):
    var_dict = randomiseVariables("r.original")
    
    ambulance = '-a'
    a = str(var_dict["a"])
    firefighter = '-ff'
    ff = str(var_dict["ff"])
    patients = '-p'
    p = str(var_dict["p"])
    hospital = '-h'
    h = str(var_dict["h"])
    bridgehead = '-bh'
    bh = str(var_dict["bh"])
    
    maxframe = '-fc'
    fc = '2000'
    
    indexName = '-indexname'
    now = datetime.now()
    dt_string = now.strftime("mcirsos-%Y-%m-%dt%H.%M.%S.%f")
    # indexName = {'-indexname' : dt_string}
    
    elastichost = '-elastichost'
    ipaddr = '192.168.0.31' #self
    #ipaddr = '192.168.25.61' #lab
    
    jarfile = getJarFile()
    
    if ("MRConsistentEmergencyResponse" in mr):
        
        fname = dt_string + "err.txt"
        f = open(fname, "w")
        timeStarted = time.time()
        inter = subprocess.Popen(['java', '-jar', jarfile, ambulance, a, firefighter, ff, patients, p, hospital, h, bridgehead, bh, indexName, dt_string, elastichost, ipaddr],  stderr=f)

        while inter.poll() is None:
            timeInter = time.time() - timeStarted
            if int(timeInter%60) > 60 :
                inter.terminate()
                return False, dt_string, ipaddr, fname, var_dict
                break

        #ret = subprocess.call(['java', '-jar', jarfile, ambulance, a, firefighter, ff, patients, p, hospital, h, bridgehead, bh, indexName, dt_string, elastichost, ipaddr],  stderr=f)
    
    return inter, dt_string, ipaddr, fname, var_dict
    #return ret, dt_string, ipaddr, fname, var_dict

def queryES(index, host):
    host_addr = 'http://' + host + ':9200/'
    client = Elasticsearch([host_addr])
    s = Search(using=client, index=index)
    request = s.source(['hash', 'author_date', 'author'])
        
    response = s.scan()
    return response   

def queryESALL(index, host):
    host_addr = 'http://' + host + ':9200/'
    client = Elasticsearch([host_addr])

    # Build a DSL Search object on the 'commits' index, 'summary' document type
    request = Search(using=client, index=index, doc_type='summary')

    # Run the Search, using the scan interface to get all resuls
    response = request.scan()
    for commit in response:
        print(commit)
        
def writeToES(host, indexname, msgBody):
    print(msgBody)
    host_addr = 'http://' + host + ':9200/'
    client = Elasticsearch([host_addr])

    # create an index in elasticsearch, ignore status code 400 (index already exists)
    client.indices.create(index=indexname, ignore=400)

    # datetimes will be serialized
    res = client.index(index=indexname, body=msgBody)
    return res

def checkMRConsistentEmergencyResponse(index, host, fname, var_dict, ret):
    indexname = "experiment_results_mc1"
    msgBody = {
        "timestamp" : "",
        "mr" : "",
        "test_type" : "r.original",
        "result" : "",
        "exception_body" : "",
        "test_indexname" : "",
        "cs_arguments" : json.dumps(var_dict)
    }
    
    
    exceptions = parseException(fname)
    if ret == True and exceptions:
        print("MRConsistentEmergencyResponse --", index, "-- result:", "FAILED DUE TO EXCEPTIONS")
        for e in exceptions:
            print(e)
            print("LINE")
        
        strExcept = str1 = ''.join(exceptions)
        exceptions_head = re.findall(r'^(.*?)Exception', strExcept, re.M)
        
        msgBody["timestamp"] = datetime.now().replace(microsecond=0).isoformat()
        msgBody["mr"] = "MRConsistentEmergencyResponse"
        msgBody["result"] = "FAILED_DUE_TO_EXCEPTIONS"
        msgBody["exception_head"] = exceptions_head[0]
        msgBody["exception_body"] = strExcept
        msgBody["test_indexname"] = index
        
        res = writeToES(host=host, indexname=indexname, msgBody=msgBody)
        print(res)
    
    elif ret == False:
        print("MRConsistentEmergencyResponse --", index, "-- result:", "FAILED DUE TO EXCEEDING RUNTIME(HANG)")
        for e in exceptions:
            print(e)
            print("LINE")
        
        strExcept = str1 = ''.join(exceptions)
        exceptions_head = re.findall(r'^(.*?)Exception', strExcept, re.M)
        
        msgBody["timestamp"] = datetime.now().replace(microsecond=0).isoformat()
        msgBody["mr"] = "MRConsistentEmergencyResponse"
        msgBody["result"] = "FAILED_DUE_TO_HANG"
        msgBody["exception_head"] = exceptions_head[0]
        msgBody["exception_body"] = strExcept
        msgBody["test_indexname"] = index
        
        res = writeToES(host=host, indexname=indexname, msgBody=msgBody)
        print(res)
        
    else :  
        response = queryES(index, host)
        returnValue = False;
        for hit in response:
            values = hit.to_dict()
            if 'world.end' in values.values():
                patients_saved = values['patients_saved']
                if (int(patients_saved) > 0):
                    returnValue = True
                    break
            
        print("MRConsistentEmergencyResponse --", index, "-- result:", returnValue)
        
        if (returnValue == True):
            msgBody["timestamp"] = datetime.now().replace(microsecond=0).isoformat()
            msgBody["mr"] = "MRConsistentEmergencyResponse"
            msgBody["result"] = "PASSED"
            msgBody["exception_body"] = "none"
            msgBody["test_indexname"] = index
        else:
            msgBody["timestamp"] = datetime.now().replace(microsecond=0).isoformat()
            msgBody["mr"] = "MRConsistentEmergencyResponse"
            msgBody["result"] = "FAILED"
            msgBody["exception_body"] = "none"
            msgBody["test_indexname"] = index
        
        res = writeToES(host=host, indexname=indexname, msgBody=msgBody)
        print(res)
        

def runTests(times, instances, testname):
    for x in range(times):
        thread_list = []
        for x in range(instances):
            thread = runner(x, testname)
            thread_list.append(thread)
        for thread in thread_list:
            thread.start()
            print("Starting Threads")
        for thread in thread_list:
            thread.join()
        time.sleep(1)

def main():
    
    # thread_list = []
    
    # for x in range(1):
    #     thread = runner(x)
    #     thread_list.append(thread)
    # for thread in thread_list:
    #     thread.start()
    #     print("Starting Threads")
    # for thread in thread_list:
    #     thread.join()
    
    # "MRConsistentPowerRegulation" | "MRConsistentReliabilityThreshold"
    runTests(50,1, "MRConsistentEmergencyResponse")

    # checkMRConsistentReliabilityThreshold(index="smartgrid-2021-05-10t01.53.54.282694", host="192.168.25.19")
    # checkMRConsistentPowerRegulation('smartgridsos')
    # queryESALL('smartgridsos')
    

if __name__ == '__main__':
    main()
