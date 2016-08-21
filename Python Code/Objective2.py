__author__ = 'Anup'
from pymongo import MongoClient

client = MongoClient()
db = client['bus']
collection = db['route']
rangeA=[17,21]
rangeB=[18,21]
rangeE=[19,26]
rangeX=[2,5]
weekDaysToConsider=["Monday","Tuesday","Wednesday","Thursday"]
outPutFile=open("C:\Users\Anup\Desktop\DM\Bus Data\Report\objective2.csv","a")
#method to find average time per stop
def avg_load_travel(routeName):
    pipeline=[{"$match":{"RouteName":routeName}},{"$group":{"_id":"$WeatherEvent"}}]
    myDict={}
    for weather in collection.aggregate(pipeline):
        pandey={"RouteName":routeName,"WeatherEvent":weather["_id"]}
       # pandey={"RouteName":routeName,"WeatherEvent":"Rain"}
        tempLoad=0
        tempTravel=0
        count=0
        for loadtime in collection.find(pandey):
          weekday=loadtime["WeekDay"]
          if weekday in weekDaysToConsider:
               try:
                tempLoad=tempLoad+float(loadtime["TotalLoadTime"])/float(loadtime["TotalStops"])
                tempTravel=tempTravel+float(loadtime["TotalTravelTime"])/(float(loadtime["TotalStops"])-1)
                count+=1
               except ZeroDivisionError:
                  print #weather["_id"]
        if count!=0:
            tempLoad=tempLoad/count
            tempTravel=tempTravel/count
            tempList=[]
            tempList.append(tempLoad)
            tempList.append(tempTravel)
            tempKey=""
            for weather in weather["_id"]:
                tempKey=tempKey+"#"+weather

            myDict[tempKey]=tempList

    return myDict

#this will return average time for route instance
def averageTimePerInstance(routeName):
    pipeline=[{"$match":{"RouteName":routeName}},{"$group":{"_id":"$WeatherEvent"}}]
    myDict={}
    tempRange=[]
    if routeName=="A":
        tempRange=rangeA
    elif routeName=="B":
        tempRange=rangeB
    elif routeName=="E":
        tempRange=rangeE
    else:
        tempRange=rangeX
    for weather in collection.aggregate(pipeline):
        pandey={"RouteName":routeName,"WeatherEvent":weather["_id"]}
        #pandey={"RouteName":routeName,"WeatherEvent":"Rain"}
        tempLoad=0
        tempTravel=0
        count=0
        for loadtime in collection.find(pandey):
          weekday=loadtime["WeekDay"]
          if weekday in weekDaysToConsider:
           if int(loadtime["TotalStops"])>=tempRange[0] and int(loadtime["TotalStops"])<=tempRange[1]:
              try:
                 tempLoad=tempLoad+float(loadtime["TotalLoadTime"])
                 tempTravel=tempTravel+float(loadtime["TotalTravelTime"])
                 count+=1
              except ZeroDivisionError:
                   print #weather["_id"]
        if count!=0:
            tempLoad=tempLoad/count
            tempTravel=tempTravel/count
            tempList=[]
            tempList.append(tempLoad)
            tempList.append(tempTravel)
            tempKey=""
            for weather in weather["_id"]:
                tempKey=tempKey+"#"+weather

            myDict[tempKey]=tempList

    return myDict

#this will calculate frequency distribution for the route  and the attribute sent
def findFrequencyDistribution(routeName,attribute):
   find={"RouteName":routeName}
   myFrequencyDict={}
   for review in collection.find(find):
       key=review[attribute]
       if myFrequencyDict.has_key(key):
        count=myFrequencyDict[key]
        count+=1
        myFrequencyDict[key]=count
       else:
         myFrequencyDict[key]=1
   return myFrequencyDict


#print averageTimePerInstance("A","")
#print avg_load_travel("A")
def genGraph(routeName,isPerStop):
    title=""
    print "Generating Graph data..."
    if isPerStop:
      routeDict=avg_load_travel(routeName)
      title="AVG LoadTime Per stop For Route "+routeName
    else:
        routeDict=averageTimePerInstance(routeName)
        title="AVG LoadTime For entire journey of Route "+routeName
    outPutFile.write(title)
    outPutFile.write("\n")
    outPutFile.write("Weather Event"+",Load Time"+",Travel time")
    outPutFile.write("\n")
    for event in routeDict:
        tempString=""
        for time in routeDict[event]:
            tempString=tempString+","+str(time)
        outPutFile.write(event+tempString)
        outPutFile.write("\n")
    outPutFile.write("\n")
    outPutFile.write("\n")
    outPutFile.close()
    print "graph data Got Generated"


genGraph("X",True)
"""
myFrequencyDict=findFrequencyDistribution("X","TotalStops")
for key in myFrequencyDict:
    print key +" : "+str(myFrequencyDict[key])
"""