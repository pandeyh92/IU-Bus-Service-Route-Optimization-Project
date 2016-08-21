__author__ = 'Anup'
import time
import pymongo
from pymongo import MongoClient
from datetime import datetime
client = MongoClient()
db = client['bus']
collection = db['route']
file=open("C:\Users\Anup\Desktop\DM\Bus Data\Report\objective4.csv","a")
accessList=["07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","00","01","02","03","04"]
weekDaysToConsider=["Monday","Tuesday","Wednesday","Thursday"]
fromDefault="2015-01-01"
toDefault="2015-12-01"
dateFormat="%Y-%m-%d"
def parseDate(date,format):
    return datetime.strptime(date, format).date()

def timeframe_find(timepoint):
    t = timepoint.split(":")
    hr = t[0]
    return hr

def load_find(val,fromDate="",toDate=""):
    frameDictionary={}
    if len(fromDate)==0:
            fromDate=fromDefault
    if len(toDate)==0:
            toDate=toDefault
    find={"RouteName":val}
    for instance in collection.find(find):
      instanceDate=parseDate(instance["Date"],dateFormat)

      if instanceDate>= parseDate(fromDate,dateFormat) and instanceDate<= parseDate(toDate,dateFormat):

         routeList = instance ["Route"]
         weekday=instance["WeekDay"]
         if weekday in weekDaysToConsider:
            for stop in routeList:
              if stop["From"] == stop["To"]:
                timepoint = stop["When"]
                frameName = timeframe_find(timepoint)
                if frameDictionary.has_key(frameName):
                    tempDict=frameDictionary[frameName]
                    if tempDict.has_key(stop["From"]):
                        tempList=tempDict[stop["From"]]
                        #for xy in tempDict[stop["From"]]:
                        tempList.append(int(stop["LoadTime"]))
                        tempDict[stop["From"]]=tempList
                    else:
                        newList2=[int(stop["LoadTime"])]
                        tempDict[stop["From"]]=newList2
                else:
                    newList=[int(stop["LoadTime"])]
                    newDict={}
                    newDict[stop["From"]]=newList
                    frameDictionary[frameName]=newDict
    return frameDictionary
"""
for show in load_find(val, frameDictionary).tempDict:
        print show
"""



"""
def calculateAverageLoad(routeNAme):
  averageDict={}
  myDict=load_find(routeNAme)
  for key in myDict:
    tempDict={}
    for stops in myDict[key]:
           length=len(myDict[key][stops])
           tempTime=0
           for loadTime in myDict[key][stops]:
                 tempTime=tempTime+loadTime
           avgLoadTime=float(tempTime)/float(length)
           tempDict[stops]=avgLoadTime
    averageDict[key]=tempDict

  for key in myDict:

     file.write("=======================================================")
     file.write("\n")
     file.write("Frame "+key)
     file.write("\n")
     for stops in myDict[key]:

           file.write("Stop "+stops)
           file.write(": Load Time "+str(myDict[key][stops]))
           file.write("\n")
  file.close()

"""
def calculateAverageLoad(routeNAme):
  averageDict={}
  myDict=load_find(routeNAme)
  for key in myDict:
    #key is time frame

    for stops in myDict[key]:

           length=len(myDict[key][stops])
           tempTime=0
           for loadTime in myDict[key][stops]:
                 tempTime=tempTime+loadTime
           avgLoadTime=float(tempTime)/float(length)
           if averageDict.has_key(stops):
             tempDict=averageDict[stops]
             tempDict[key]=avgLoadTime
             averageDict[stops]=tempDict
           else:
               tempDictNew={}
               tempDictNew[key]=avgLoadTime
               averageDict[stops]=tempDictNew
  return averageDict


#calculateAverageLoad("A")


"""
def printValuse(routeName):
  myDict=calculateAverageLoad(routeName)
  for key in myDict:

    file.write("=======================================================")
    file.write("\n")
    file.write("Stop Name "+key)
    file.write("\n")
    for stops in myDict[key]:

           file.write("Time Frame "+stops)
           file.write(": Load Time "+str(myDict[key][stops]))
           file.write("\n")

printValuse("A")
file.close()
"""
def convertIntoAmOrPm(hour):
    if int(hour)>11:
        return hour+" PM"
    else:
        return hour+" AM"

def writeIntoCSV(routeName):

  myDict=calculateAverageLoad(routeName)

  count=0
  print "Generating Data for report ......"
  file.write("For Stop "+routeName)
  file.write("\n")
  for stop in myDict:
    if count%3==0:
        file.write("\n")
        file.write("Stop NAme")
        for tFrame in accessList:
           file.write(","+convertIntoAmOrPm(tFrame))
        file.write("\n")
    count+=1
    file.write(stop)
    for timeFrame in accessList:
        if myDict[stop].has_key(timeFrame):
              file.write(",")
              file.write(str(myDict[stop][timeFrame]))
    file.write("\n")

  file.write("\n")
  file.write("\n")
  file.write("\n")
  file.close()
  print("Data Got Generated.")

writeIntoCSV("X")

