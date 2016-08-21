__author__ = 'Anup'
import pymongo
from datetime import datetime
from pymongo import MongoClient



fromDefault="2015-01-01"
toDefault="2015-12-01"
dateFormat="%Y-%m-%d"
def parseDate(date,format):
    return datetime.strptime(date, format).date()

def get_actual_time(val,fromDate="",toDate=""):
        client = MongoClient()
        db = client['bus']
        collection = db['route']
        Mydict = {'A' : ['Stadium(A)','Well\'s Library','3rd & Jordan','IMU',"Stadium(A)"], 'B':['Fisher Court()','10th & Jordan','3rd & Jordan','10th and Jordan()','Fisher Court()'], 'E':['Evermann','Union & 10th','Wilkie','3rd & Jordan','IMU','Well\'s Library','College Mall()'], 'X': ['Stadium(A)','IMU']}
        if len(fromDate)==0:
            fromDate=fromDefault
        if len(toDate)==0:
            toDate=toDefault
        myDict={}
        listStops=Mydict[val]
        find = {"RouteName":val}
        for pandey in  collection.find(find):
          instanceDate=parseDate(pandey["Date"],dateFormat)

          if instanceDate>= parseDate(fromDate,dateFormat) and instanceDate<= parseDate(toDate,dateFormat):
             scheduleName="NA"
             if len(pandey["ScheduleName"])!=0:
                 scheduleName=pandey["ScheduleName"]
             key=pandey["Date"]+"@"+pandey["StartTime"]+"@"+pandey["WeekDay"]+"@"+scheduleName
             routeList=pandey["Route"]
             count=0
             tempTimeList=[]
             for tempDict in routeList:
                 if count>len(listStops)-1:
                     break
                 if count==0:
                     count+=1
                     if tempDict["From"]==tempDict["To"]:
                         tempTimeList.append(tempDict["When"])
                 else:
                     if listStops[count] in tempDict["To"]:
                         count+=1
                         tempTimeList.append(tempDict["When"])
             myDict[key]=tempTimeList
        client.close()
        return myDict







tempDict=get_actual_time("A","2015-03-01","2015-04-30")
for key in tempDict:
    print key+":"+str(tempDict[key])
    arr=key.split("@")
    print "=================================================================================="

"""
present=datetime.strptime('2014-05-28', '%Y-%m-%d').date()
past=datetime.strptime('2014-12-05', '%Y-%m-%d').date()
string=""
if len(string)==0:
    print len(string)
if present>past:
    print "great"
elif present==past:
    print "equal"
else:
    print "small"
"""

    #db.route.find({"RouteName":"A", "Date":"2015-06-01"}).pretty()

