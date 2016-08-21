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
file=open("C:\Users\Anup\Desktop\DM\Bus Data\Report\objective2Extended.csv","a")
#method to find average time per stop
def avg_load_travel(routeName):
    if len(routeName)==0:
        pipeline=[{"$group":{"_id":"$WeatherEvent"}}]
    else:
        pipeline=[{"$match":{"RouteName":routeName}},{"$group":{"_id":"$WeatherEvent"}}]

    myDict={}
    for weather in collection.aggregate(pipeline):
        if len(routeName)==0:
             pandey={"WeatherEvent":weather["_id"]}
        else:
            pandey={"RouteName":routeName,"WeatherEvent":weather["_id"]}
       # pandey={"RouteName":routeName,"WeatherEvent":"Rain"}
        percipitationDict={}
        for loadtime in collection.find(pandey):
          weekday=loadtime["WeekDay"]
          if weekday in weekDaysToConsider:
             perci=loadtime["Percipitation"]
             try:
               if percipitationDict.has_key(perci):
                   timeList=percipitationDict[perci]
                   tempLoad=timeList[0]
                   tempTravel=timeList[1]
                   count=timeList[2]
                   tempLoad=tempLoad+float(loadtime["TotalLoadTime"])/float(loadtime["TotalStops"])
                   tempTravel=tempTravel+float(loadtime["TotalTravelTime"])/(float(loadtime["TotalStops"])-1)
                   count+=1
                   timeList[:]=[]
                   timeList.append(tempLoad)
                   timeList.append(tempTravel)
                   timeList.append(count)
                   percipitationDict[perci]=timeList
               else:
                   timeList=[]
                   timeList.append((float(loadtime["TotalLoadTime"])/float(loadtime["TotalStops"])))
                   timeList.append((float(loadtime["TotalTravelTime"]))/(float(loadtime["TotalStops"])-1))
                   timeList.append(1)
                   tempdict={perci:timeList}
                   percipitationDict.update(tempdict)
             except ZeroDivisionError:
                print #weather["_id"]
        tempKey=""
        for weather in weather["_id"]:
            tempKey=tempKey+"#"+weather
        finaldict={}
        for per in percipitationDict:
            tempList=[]
            tempTimeList=percipitationDict[per]
            tempList.append(tempTimeList[0]/tempTimeList[2])
            tempList.append(tempTimeList[1]/tempTimeList[2])
            finaldict.update({per:tempList})


        myDict[tempKey]=finaldict

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
        percipitationDict={}
        for loadtime in collection.find(pandey):

          weekday=loadtime["WeekDay"]
          if weekday in weekDaysToConsider:
            if int(loadtime["TotalStops"])>=tempRange[0] and int(loadtime["TotalStops"])<=tempRange[1]:
              perci=loadtime["Percipitation"]
              try:
                if percipitationDict.has_key(perci):
                   timeList=percipitationDict[perci]
                   tempLoad=timeList[0]
                   tempTravel=timeList[1]
                   count=timeList[2]
                   tempLoad=tempLoad+float(loadtime["TotalLoadTime"])
                   tempTravel=tempTravel+float(loadtime["TotalTravelTime"])
                   count+=1
                   timeList[:]=[]
                   timeList.append(tempLoad)
                   timeList.append(tempTravel)
                   timeList.append(count)
                   percipitationDict[perci]=timeList
                else:
                   timeList=[]
                   timeList.append((float(loadtime["TotalLoadTime"])))
                   timeList.append((float(loadtime["TotalTravelTime"])))
                   timeList.append(1)
                   tempdict={perci:timeList}
                   percipitationDict.update(tempdict)
              except ZeroDivisionError:
                   print #weather["_id"]

        tempKey=""
        for weather in weather["_id"]:
            tempKey=tempKey+"#"+weather
        finaldict={}
        for per in percipitationDict:
            tempList=[]
            tempTimeList=percipitationDict[per]
            tempList.append(tempTimeList[0]/tempTimeList[2])
            tempList.append(tempTimeList[1]/tempTimeList[2])
            finaldict.update({per:tempList})

        myDict[tempKey]=finaldict

    return myDict



#print averageTimePerInstance("A","")
#print avg_load_travel("A","")



def findWEventFD(routeName):

    pipeline=[{"$match":{"RouteName":routeName}},{"$group":{"_id":"$WeatherEvent"}}]
    myDict={}
    for weather in collection.aggregate(pipeline):
        pandey={"RouteName":routeName,"WeatherEvent":weather["_id"]}

        perci=[]
        for loadtime in collection.find(pandey):
          weekday=loadtime["WeekDay"]

          if weekday in weekDaysToConsider:
              per=loadtime["Percipitation"]
              if per not in perci:
                 perci.append(per)



        tempKey=""
        for weather in weather["_id"]:
            tempKey=tempKey+"#"+weather

        myDict[tempKey]=perci

    return myDict
"""
myFrequencyDict=findFrequencyDistribution("X","TotalStops")
for key in myFrequencyDict:
    print key +" : "+str(myFrequencyDict[key])
"""

#print findWEventFD("A")

#print avg_load_travel("A")
#print averageTimePerInstance("A")

def writeIntoCSV(whTTocall,routeName):
    myDict={}
    if whTTocall==1:
        myDict=avg_load_travel(routeName)
    else:
        myDict=averageTimePerInstance(routeName)
    print myDict

    if len(myDict)!=0:
        for key in myDict:
            file.write(key)
            file.write("\n")
            file.write("Precipitation"+","+"LoadTime"+","+"Traveltime")
            file.write("\n")
            for perKey in myDict[key]:
                tempList=myDict[key][perKey]
                file.write(perKey+","+str(tempList[0])+","+str(tempList[1]))
                file.write("\n")

    file.close()


writeIntoCSV(1,"")

