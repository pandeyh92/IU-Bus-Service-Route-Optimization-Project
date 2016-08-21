__author__ = 'Anup'
from pymongo import MongoClient
client = MongoClient()
db = client['bus']

"""
routeA=open("C:\Users\Anup\Desktop\DM\Bus Data\RouteAVariance.txt")
myDict={}
for line in routeA:
    split1=line.split("@")
    split2=split1[2].split(":")
    if myDict.has_key(split2[0]):
        count=int(myDict[split2[0]])
        count=count+int(split2[1].strip())
        myDict[split2[0]]=count
    else:
        myDict[split2[0]]=int(split2[1].strip())


print myDict
"""

routeTable=db['route']
find={}
count=0
for route in routeTable.find(find):
    schedName=route["ScheduleName"]
    if len(schedName) !=0:
        count+=1
        print route


print "Total count ",count


