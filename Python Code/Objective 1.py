import  MongoQuery as mon

pathRouteA1='C:\Users\Shalabh\Desktop\Bus Data\Schedule_Data_A1.csv'
pathRouteA2='C:\Users\Shalabh\Desktop\Bus Data\Schedule_Data_A2.csv'
pathRouteA3='C:\Users\Shalabh\Desktop\Bus Data\Schedule_Data_A3.csv'
pathRouteA4='C:\Users\Shalabh\Desktop\Bus Data\Schedule_Data_A4.csv'
pathRouteA5='C:\Users\Shalabh\Desktop\Bus Data\Schedule_Data_A5.csv'
pathRouteA6='C:\Users\Shalabh\Desktop\Bus Data\Schedule_Data_A6.csv'
pathRouteA7='C:\Users\Shalabh\Desktop\Bus Data\Schedule_Data_A7.csv'
pathRouteB1='C:\Users\Shalabh\Desktop\Bus Data\Schedule_Data_B1.csv'
pathRouteB2='C:\Users\Shalabh\Desktop\Bus Data\Schedule_Data_B2.csv'
pathRouteB3='C:\Users\Shalabh\Desktop\Bus Data\Schedule_Data_B3.csv'
pathRouteB4='C:\Users\Shalabh\Desktop\Bus Data\Schedule_Data_B4.csv'
pathRouteB5='C:\Users\Shalabh\Desktop\Bus Data\Schedule_Data_B5.csv'
pathRouteE1='C:\Users\Shalabh\Desktop\Bus Data\Schedule_Data_E1.csv'
pathRouteE2='C:\Users\Shalabh\Desktop\Bus Data\Schedule_Data_E2.csv'
pathRouteE3='C:\Users\Shalabh\Desktop\Bus Data\Schedule_Data_E3.csv'
pathRouteX1='C:\Users\Shalabh\Desktop\Bus Data\Schedule_Data_X1.csv'
pathRouteX2='C:\Users\Shalabh\Desktop\Bus Data\Schedule_Data_X2.csv'

routBTravelSequence=["Fisher Court","Jordan & 10th","3rd & Jordan","Jordan & 10th","Fisherr Cour"]
def time24hr(tstr):
	s = tstr.split(':')

	am = {'12':'00','1':'01','2':'02','3':'03','4':'04','5':'05','6':'06','7':'07','8':'08','9':'09','10':'10','11':'11'}
	pm = {'12':'12','1':'13','2':'14','3':'15','4':'16','5':'17','6':'18','7':'19','8':'20','9':'21','10':'22','11':'23'}
	if 'AM' in tstr:
               t=am[s[0]] +":" +s[1][:2]
               return t
	elif 'PM' in tstr:
                   t=pm[s[0]]+":" + s[1][:2]
                   return t
"""
def populateSchedule(routeName,arg):
    count=0
    wholeDict={}
    orderedList=[]
    path=""
    start=""
    if routeName in "pathRouteA":
        path=pathRouteA
        start="Load"
    elif routeName in "pathRouteB":
        path=pathRouteB
        start="Fisher Court"
    elif routeName in "pathRouteE":
        path=pathRouteE
        start="Evermann"
    elif routeName in "pathRouteX":
        path=pathRouteX
        start="Load"
    with open(path) as fp:
        for line in fp:
            tempList= line.split(',')
            if start in tempList[1]:
                count = count + 1
                if (count > 1):
                        wholeDict.update({a:myDict})
                myDict={}
                z=time24hr(tempList[0])
                myDict.update({z: tempList[1].strip("\n")})
                a = z
                orderedList.append(a)
            else:
                z=time24hr(tempList[0])
                myDict.update({z:tempList[1].strip("\n")})
    print "List having expected timings for" ,routeName,":",orderedList
    print "wholeDict for" , routeName,":",wholeDict
    if arg==1:
        return  orderedList
    elif arg==2:
        return  wholeDict
"""

def populateSchedule(routeName):
    count=0
    Route_wholeDict={}
    Route_wholeList={}
    pathList=[]
    start=""
    if routeName in "pathRouteA":
        pathList=[pathRouteA1,pathRouteA2,pathRouteA3,pathRouteA4,pathRouteA5,pathRouteA6,pathRouteA7]
        start="Load"
    elif routeName in "pathRouteB":
        pathList=[pathRouteB1,pathRouteB2,pathRouteB3,pathRouteB4,pathRouteB5]
        start="Fisher Court"
    elif routeName in "pathRouteE":
        pathList=[pathRouteE1,pathRouteE2,pathRouteE3]
        start="Evermann"
    elif routeName in "pathRouteX":
        pathList=[pathRouteX1,pathRouteX2]
        start="Load"
    for sched in pathList:
        z=str(sched)[-6:]
        schd=z.split('.')
        #print "For schedule:",sched
        #print "z:",z
        #print "For schedule:",schd[0]
        wholeDict={}
        orderedList=[]
        with open(sched) as fp:
            for line in fp:
                tempList= line.split(',')
                if start in tempList[1]:
                    count = count + 1
                    if (count > 1):
                        wholeDict.update({a:myDict})
                    myDict={}
                    z=time24hr(tempList[0])
                    myDict.update({z: tempList[1].strip("\n")})
                    a = z
                    orderedList.append(a)
                else:
                    z=time24hr(tempList[0])
                    myDict.update({z:tempList[1].strip("\n")})
        Route_wholeDict.update({schd[0]:wholeDict})
        Route_wholeList.update({schd[0]:orderedList})
    #print "List having expected starting timings for first stop of each trip for different schedules" ,routeName,":",Route_wholeList
    #print "List having expected timings for each stop of each trip for different schedules" , routeName,":",Route_wholeDict
    return  Route_wholeList,Route_wholeDict

#def calculateClosest(routeName,dict1):
def calculateClosest(routeName,dict1):
    orderedList1={}
    wholeDict={}
    closest_time={}
    orderedList1,wholeDict=populateSchedule(routeName)
    #dict1=mon.get_actual_time(routeName[len(routeName)-1],)
    #print " List having expected timings for" ,routeName,":",orderedList1
    #print " List having actual timings for" ,routeName,":",dict1
    count=1
    schd=""
    for i in  dict1:
        sorted_list=[]
        #print "for  key",i
        elem= i.split('@')
        actual=elem[1].split(':')
        schd=elem[3]
        #print "schd=",schd
        if schd in ("A7","NA","X3"):
            continue
        else:
            tactual=actual[0]+":"+actual[1]
            sorted_list=[]
            #print "actual time=",tactual
            tactual_min =int((int(actual[0])*60) + int(actual[1]))
            #print "tactual_min=",tactual_min
            for key in orderedList1:
                if key in schd:
                    #print "key= ",key
                    sorted_list=orderedList1[key]
                    break
            #print "sorted list=",sorted_list
            first=sorted_list[0].split(':')
            key_expected=sorted_list[0]
            first_min=int((int(first[0])*60)+int(first[1]))
            least_diff=tactual_min - first_min
            least_diff=abs(least_diff)
            for texpected in sorted_list:
                k=texpected.split(':')
                texpected_min=int((int(k[0])*60)+int(k[1]))
                diff=tactual_min-texpected_min
                diff=abs(diff)
                #print "texpected:",texpected
                #print "diff:",diff
                #print "least_diff:",least_diff
                if diff < least_diff:
                    least_diff=diff
                    key_expected=texpected
                elif diff > least_diff:
                    break
        #print "The closest time to ",i," is ",key_expected
        closest_time.update({i:key_expected})
    print " Dictionary having actual timing with closest schedules time for " ,routeName," is :",closest_time
    return closest_time

def calculateVariance(routeName,fromdate,toDate):
    actual_time_dict1={}
    closest_time_dict2={}
    exptd_time_dict3={}
    exptd_time_list1={}
    variance={}
    bad_instances=[]
    actual_time_dict1=mon.get_actual_time(routeName[len(routeName)-1],fromdate,toDate)
    print " Dictionary having actual timings for " ,routeName,":",actual_time_dict1
    closest_time_dict2=calculateClosest(routeName,actual_time_dict1)
    print " Dictionary having actual timing with closest schedules time " ,routeName,":",closest_time_dict2
    exptd_time_list1,exptd_time_dict3=populateSchedule(routeName)
    print " Dictionary having expected timings for " ,routeName,":",exptd_time_dict3
    for i in  closest_time_dict2:  #mapping of actual time(key:date@start-time format) with the expected time(value)
        delay=0
        key1=i
        elem= key1.split('@')
        schd=elem[3]
        value1=closest_time_dict2[i]
        #print "key1::the actual time :",key1
        #print "value1::the expected time for the above actual time is :",value1
        list=[]
        dict={}
        dict1={}
        for j in actual_time_dict1:   #taking out the list of the actual time for a particular time stamp(key)
            if key1 in j:
                list=actual_time_dict1[j]
                #print "value of j which should be matching key1 is:",j
                #print "the corresponding list is ", list
                break
        for k in exptd_time_dict3:     #taking out the dictionary for the expected time for a particular time stamp(key)
            if schd in k:
                dict=exptd_time_dict3[k]
                #print "value of k which should be matching value1 is:",k
                #print "the corresponding dictionary is ", dict
                for l in dict:
                    if value1 in l:
                        dict1=dict[l]
                        break
                break
        #print "i=",i
        #print "list=",list
        #print "dict1=",dict1
        #########Calculating the difference for Route A############
        if ((routeName in "pathRouteA") and (len(list)==5)):
            for key2 in dict1:
                if str(key2) not in "None":
                    #print "dict[key2]:",dict[key2]
                    if "Stadium ()" in dict1[key2]:
                        #print "dict[key2] in Stadium():",dict[key2]
                        actual=list[4].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
                    elif "Stadium" in dict1[key2]:
                        #print "dict[key2] in Stadium:",dict[key2]
                        actual=list[0].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
                    elif "Well's Library" in dict1[key2]:
                        actual=list[1].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
                    elif "3rd & Jordan" in dict1[key2]:
                        actual=list[2].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
                    elif "IMU" in dict1[key2]:
                        actual=list[3].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
            delay=delay/len(list)
            variance.update({key1:delay})
        elif ((routeName in "pathRouteA") and (len(list)!=5)):
            bad_instances.append(key1)
        #########Calculating the difference for Route A ends############
        #########Calculating the difference for Route B############
        if ((routeName in "pathRouteB") and (len(list)==5)):
            for key2 in dict1:
            #for key2 in routBTravelSequence:
                #if count>len(list)-1:
                 #   break
                if str(key2) not in "None":
                    #print "dict[key2]:",dict[key2]
                    if "Fisher Court" in dict1[key2]:
                        #print "dict[key2] in Fisher Court:",dict[key2]
                        actual=list[0].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
                    elif "Jordan & 10th" in dict1[key2]:
                        #print "dict[key2] in Stadium:",dict[key2]
                        actual=list[1].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
                    elif "3rd & Jordan" in dict1[key2]:
                        actual=list[2].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
                    elif "Jordann & 10th" in dict1[key2]:
                        #print "dict[3] in Stadium:",dict[key2]
                        actual=list[3].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
                    elif "Fisherr Court" in dict1[key2]:
                        #print "dict[key2] in Stadium:",dict[key2]
                        actual=list[4].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
            delay=delay/len(list)
            variance.update({key1:delay})
        elif ((routeName in "pathRouteB") and (len(list)==4)):
            for key2 in dict1:
            #for key2 in routBTravelSequence:
                #if count>len(list)-1:
                 #   break
                if str(key2) not in "None":
                    #print "dict[key2]:",dict[key2]
                    if "Fisher Court" in dict1[key2]:
                        #print "dict[key2] in Fisher Court:",dict[key2]
                        actual=list[0].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
                    elif "Jordan & 10th" in dict1[key2]:
                        #print "dict[key2] in Stadium:",dict[key2]
                        actual=list[1].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
                    elif "3rd & Jordan" in dict1[key2]:
                        actual=list[2].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
                    elif "Jordann & 10th" in dict1[key2]:
                        #print "dict[3] in Stadium:",dict[key2]
                        actual=list[3].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
            delay=delay/len(list)
            variance.update({key1:delay})
        elif ((routeName in "pathRouteB") and (len(list)==3)):
            for key2 in dict1:
                if str(key2) not in "None":
                    #print "dict[key2]:",dict[key2]
                    if "Fisher Court" in dict1[key2]:
                        #print "dict[key2] in Fisher Court:",dict[key2]
                        actual=list[0].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
                    elif "Jordan & 10th" in dict1[key2]:
                        #print "dict[key2] in Stadium:",dict[key2]
                        actual=list[1].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
                    elif "3rd & Jordan" in dict1[key2]:
                        actual=list[2].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
            delay=delay/len(list)
            variance.update({key1:delay})
        elif ((routeName in "pathRouteB") and ((len(list)==1)) or (len(list)==2)):
            bad_instances.append(key1)
        #########Calculating the difference for Route B ends############
        #########Calculating the difference for RouteE starts############
        if ((routeName in "pathRouteE") and ((len(list)==7) or (len(list)==6))):
            for key2 in dict1:
                if str(key2) not in "None":
                    #print "dict[key2]:",dict[key2]
                    if "Evermann" in dict1[key2]:
                        #print "dict[key2] in Stadium():",dict[key2]
                        actual=list[0].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
                    elif "Union & 10th" in dict1[key2]:
                        #print "dict[key2] in Stadium:",dict[key2]
                        actual=list[1].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
                    elif "Willkie" in dict1[key2]:
                        actual=list[2].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
                    elif "3rd & Jordan" in dict1[key2]:
                        actual=list[3].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
                    elif "IMU" in dict1[key2]:
                        actual=list[4].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
                    elif "Wells Library" in dict1[key2]:
                        actual=list[5].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
            delay=delay/len(list)
            variance.update({key1:delay})
        elif ((routeName in "pathRouteE") and ((len(list)!=7) or (len(list)!=6))):
            bad_instances.append(key1)
        #########Calculating the difference for Route E ends############
        #########Calculating the difference for Route X############
        if ((routeName in "pathRouteX") and ((len(list)==1) or (len(list)==2))):
            for key2 in dict1:
                if str(key2) not in "None":
                    #print "dict[key2]:",dict[key2]
                    if "Stadium" in dict1[key2]:
                        #print "dict[key2] in Stadium:",dict[key2]
                        actual=list[0].split(':')
                        actual_mins=int(int(int(actual[0])*60)+int(actual[1]))
                        expected=key2.split(':')
                        expected_mins=int(int(int(expected[0])*60)+int(expected[1]))
                        delay=delay+int(expected_mins-actual_mins)
            delay=delay/len(list)
            variance.update({key1:delay})
        elif((routeName in "pathRouteX") and ((len(list)!=1) or (len(list)!=2))):
            bad_instances.append(key1)
        #########Calculating the difference for Route X ends############
    print "the actual time stamps with the variances for ",routeName," are:",variance
    print "the bad time stamps for ",routeName," are:",bad_instances
    return variance



def GroupBy(routeName,fromdate="",toDate="",isByDayOfWeek=True ,isByTimeOfDay=True):
    if isByDayOfWeek and isByTimeOfDay :
        #group based on both dayOfweek and Time of day
        GroupByTimeandDay(routeName,fromdate,toDate)
    elif isByDayOfWeek :
        #group based on dayOfweek
        GroupByDayofWeek(routeName,fromdate,toDate)
    elif isByTimeOfDay :
        #group based on Time of day
        GroupByTimeofDay(routeName,fromdate,toDate)
    return

def GroupByDayofWeek(routeName,fromdate,toDate):
    varianceDict=calculateVariance(routeName,fromdate,toDate)
    countMon=0
    varMon=0
    countTue=0
    varTue=0
    countWed=0
    varWed=0
    countThur=0
    varThur=0
    for key in varianceDict:
        timestamp=key.split("@")
        day=timestamp[2]
        sched=timestamp[3]
        if day in "Monday":
            countMon+=1
            varMon=varMon+varianceDict[key]
        elif day in "Tuesday":
            countTue+=1
            varTue=varTue+varianceDict[key]
        elif day in "Wednesday":
            countWed+=1
            varWed=varWed+varianceDict[key]
        elif day in "Thursday":
            countThur+=1
            varThur=varThur+varianceDict[key]
    print "For route ", routeName
    print "Average variance in mins on Mondays is:",float(varMon)/countMon
    print "Average variance in mins on Tuesdays is:",float(varTue)/countTue
    print "Average variance in mins on Wednesdays is:",float(varWed)/countWed
    print "Average variance in mins on Thursdays is:",float(varThur)/countThur
    return

def GroupByTimeofDay(routeName,fromdate,toDate):
    varianceDict=calculateVariance(routeName,fromdate,toDate)
    count=[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    sum_variance=[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    for key in varianceDict:
        timestamp=key.split("@")
        time=timestamp[1]
        t=time.split(":")
        hr=int(t[0])
        if hr==7:
            count[0]+=1
            sum_variance[0]=sum_variance[0]+varianceDict[key]
        elif hr==8:
            count[1]+=1
            sum_variance[1]=sum_variance[1]+varianceDict[key]
        elif hr==9:
            count[2]+=1
            sum_variance[2]=sum_variance[2]+varianceDict[key]
        elif hr==10:
            count[3]+=1
            sum_variance[3]=sum_variance[3]+varianceDict[key]
        elif hr==11:
            count[4]+=1
            sum_variance[4]=sum_variance[4]+varianceDict[key]
        elif hr==12:
            count[5]+=1
            sum_variance[5]=sum_variance[5]+varianceDict[key]
        elif hr==13:
            count[6]+=1
            sum_variance[6]=sum_variance[6]+varianceDict[key]
        elif hr==14:
            count[7]+=1
            sum_variance[7]=sum_variance[7]+varianceDict[key]
        elif hr==15:
            count[8]+=1
            sum_variance[8]=sum_variance[8]+varianceDict[key]
        elif hr==16:
            count[9]+=1
            sum_variance[9]=sum_variance[9]+varianceDict[key]
        elif hr==17:
            count[10]+=1
            sum_variance[10]=sum_variance[10]+varianceDict[key]
        elif hr==18:
            count[11]+=1
            sum_variance[11]=sum_variance[11]+varianceDict[key]
        elif hr==19:
            count[12]+=1
            sum_variance[12]=sum_variance[12]+varianceDict[key]
        elif hr==20:
            count[13]+=1
            sum_variance[13]=sum_variance[13]+varianceDict[key]
        elif hr==21:
            count[14]+=1
            sum_variance[14]=sum_variance[14]+varianceDict[key]
        elif hr==22:
            count[15]+=1
            sum_variance[15]=sum_variance[15]+varianceDict[key]
        elif hr==23:
            count[16]+=1
            sum_variance[16]=sum_variance[16]+varianceDict[key]
    cnt=0
    for i in sum_variance:
        if count[cnt]==0:
            count[cnt]+=1
        avg_variance=float(sum_variance[cnt])/count[cnt]
        print "----------------------------------------"
        #print "The total number of instances of time slot(",(cnt+7),"-",(cnt+8),")hrs is=",count[cnt]
        print "The average variance for ",routeName," in time-slot(",(cnt+7),"-",(cnt+8),")hrs is=",avg_variance
        cnt+=1
    return

def GroupByTimeandDay(routeName,fromdate,toDate):
    varianceDict=calculateVariance(routeName,fromdate,toDate)
    count1=[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    sum_variance1=[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    count2=[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    sum_variance2=[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    count3=[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    sum_variance3=[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    count4=[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    sum_variance4=[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    for key in varianceDict:
        timestamp=key.split("@")
        day=timestamp[2]
        time=timestamp[1]
        t=time.split(":")
        hr=int(t[0])
        if day in "Monday":
            if hr==7:
                count1[0]+=1
                sum_variance1[0]=sum_variance1[0]+varianceDict[key]
            elif hr==8:
                count1[1]+=1
                sum_variance1[1]=sum_variance1[1]+varianceDict[key]
            elif hr==9:
                count1[2]+=1
                sum_variance1[2]=sum_variance1[2]+varianceDict[key]
            elif hr==10:
                count1[3]+=1
                sum_variance1[3]=sum_variance1[3]+varianceDict[key]
            elif hr==11:
                count1[4]+=1
                sum_variance1[4]=sum_variance1[4]+varianceDict[key]
            elif hr==12:
                count1[5]+=1
                sum_variance1[5]=sum_variance1[5]+varianceDict[key]
            elif hr==13:
                count1[6]+=1
                sum_variance1[6]=sum_variance1[6]+varianceDict[key]
            elif hr==14:
                count1[7]+=1
                sum_variance1[7]=sum_variance1[7]+varianceDict[key]
            elif hr==15:
                count1[8]+=1
                sum_variance1[8]=sum_variance1[8]+varianceDict[key]
            elif hr==16:
                count1[9]+=1
                sum_variance1[9]=sum_variance1[9]+varianceDict[key]
            elif hr==17:
                count1[10]+=1
                sum_variance1[10]=sum_variance1[10]+varianceDict[key]
            elif hr==18:
                count1[11]+=1
                sum_variance1[11]=sum_variance1[11]+varianceDict[key]
            elif hr==19:
                count1[12]+=1
                sum_variance1[12]=sum_variance1[12]+varianceDict[key]
            elif hr==20:
                count1[13]+=1
                sum_variance1[13]=sum_variance1[13]+varianceDict[key]
            elif hr==21:
                count1[14]+=1
                sum_variance1[14]=sum_variance1[14]+varianceDict[key]
            elif hr==22:
                count1[15]+=1
                sum_variance1[15]=sum_variance1[15]+varianceDict[key]
            elif hr==23:
                count1[16]+=1
                sum_variance1[16]=sum_variance1[16]+varianceDict[key]
        elif day in "Tuesday":
            if hr==7:
                count2[0]+=1
                sum_variance2[0]=sum_variance2[0]+varianceDict[key]
            elif hr==8:
                count2[1]+=1
                sum_variance2[1]=sum_variance2[1]+varianceDict[key]
            elif hr==9:
                count2[2]+=1
                sum_variance2[2]=sum_variance2[2]+varianceDict[key]
            elif hr==10:
                count2[3]+=1
                sum_variance2[3]=sum_variance2[3]+varianceDict[key]
            elif hr==11:
                count2[4]+=1
                sum_variance2[4]=sum_variance2[4]+varianceDict[key]
            elif hr==12:
                count2[5]+=1
                sum_variance2[5]=sum_variance2[5]+varianceDict[key]
            elif hr==13:
                count2[6]+=1
                sum_variance2[6]=sum_variance2[6]+varianceDict[key]
            elif hr==14:
                count2[7]+=1
                sum_variance2[7]=sum_variance2[7]+varianceDict[key]
            elif hr==15:
                count2[8]+=1
                sum_variance2[8]=sum_variance2[8]+varianceDict[key]
            elif hr==16:
                count2[9]+=1
                sum_variance2[9]=sum_variance2[9]+varianceDict[key]
            elif hr==17:
                count2[10]+=1
                sum_variance2[10]=sum_variance2[10]+varianceDict[key]
            elif hr==18:
                count2[11]+=1
                sum_variance2[11]=sum_variance2[11]+varianceDict[key]
            elif hr==19:
                count2[12]+=1
                sum_variance2[12]=sum_variance2[12]+varianceDict[key]
            elif hr==20:
                count2[13]+=1
                sum_variance2[13]=sum_variance2[13]+varianceDict[key]
            elif hr==21:
                count2[14]+=1
                sum_variance2[14]=sum_variance2[14]+varianceDict[key]
            elif hr==22:
                count2[15]+=1
                sum_variance2[15]=sum_variance2[15]+varianceDict[key]
            elif hr==23:
                count2[16]+=1
                sum_variance2[16]=sum_variance2[16]+varianceDict[key]
        elif day in "Wednesday":
            if hr==7:
                count3[0]+=1
                sum_variance3[0]=sum_variance3[0]+varianceDict[key]
            elif hr==8:
                count3[1]+=1
                sum_variance3[1]=sum_variance3[1]+varianceDict[key]
            elif hr==9:
                count3[2]+=1
                sum_variance3[2]=sum_variance3[2]+varianceDict[key]
            elif hr==10:
                count3[3]+=1
                sum_variance3[3]=sum_variance3[3]+varianceDict[key]
            elif hr==11:
                count3[4]+=1
                sum_variance3[4]=sum_variance3[4]+varianceDict[key]
            elif hr==12:
                count3[5]+=1
                sum_variance3[5]=sum_variance3[5]+varianceDict[key]
            elif hr==13:
                count3[6]+=1
                sum_variance3[6]=sum_variance3[6]+varianceDict[key]
            elif hr==14:
                count3[7]+=1
                sum_variance3[7]=sum_variance3[7]+varianceDict[key]
            elif hr==15:
                count3[8]+=1
                sum_variance3[8]=sum_variance3[8]+varianceDict[key]
            elif hr==16:
                count3[9]+=1
                sum_variance3[9]=sum_variance3[9]+varianceDict[key]
            elif hr==17:
                count3[10]+=1
                sum_variance3[10]=sum_variance3[10]+varianceDict[key]
            elif hr==18:
                count3[11]+=1
                sum_variance3[11]=sum_variance3[11]+varianceDict[key]
            elif hr==19:
                count3[12]+=1
                sum_variance3[12]=sum_variance3[12]+varianceDict[key]
            elif hr==20:
                count3[13]+=1
                sum_variance3[13]=sum_variance3[13]+varianceDict[key]
            elif hr==21:
                count3[14]+=1
                sum_variance3[14]=sum_variance3[14]+varianceDict[key]
            elif hr==22:
                count3[15]+=1
                sum_variance3[15]=sum_variance3[15]+varianceDict[key]
            elif hr==23:
                count3[16]+=1
                sum_variance3[16]=sum_variance3[16]+varianceDict[key]
        elif day in "Thursday":
            if hr==7:
                count4[0]+=1
                sum_variance4[0]=sum_variance4[0]+varianceDict[key]
            elif hr==8:
                count4[1]+=1
                sum_variance4[1]=sum_variance4[1]+varianceDict[key]
            elif hr==9:
                count4[2]+=1
                sum_variance4[2]=sum_variance4[2]+varianceDict[key]
            elif hr==10:
                count4[3]+=1
                sum_variance4[3]=sum_variance4[3]+varianceDict[key]
            elif hr==11:
                count4[4]+=1
                sum_variance4[4]=sum_variance4[4]+varianceDict[key]
            elif hr==12:
                count4[5]+=1
                sum_variance4[5]=sum_variance4[5]+varianceDict[key]
            elif hr==13:
                count4[6]+=1
                sum_variance4[6]=sum_variance4[6]+varianceDict[key]
            elif hr==14:
                count4[7]+=1
                sum_variance4[7]=sum_variance4[7]+varianceDict[key]
            elif hr==15:
                count4[8]+=1
                sum_variance4[8]=sum_variance4[8]+varianceDict[key]
            elif hr==16:
                count4[9]+=1
                sum_variance4[9]=sum_variance4[9]+varianceDict[key]
            elif hr==17:
                count4[10]+=1
                sum_variance4[10]=sum_variance4[10]+varianceDict[key]
            elif hr==18:
                count4[11]+=1
                sum_variance4[11]=sum_variance4[11]+varianceDict[key]
            elif hr==19:
                count4[12]+=1
                sum_variance4[12]=sum_variance4[12]+varianceDict[key]
            elif hr==20:
                count4[13]+=1
                sum_variance4[13]=sum_variance4[13]+varianceDict[key]
            elif hr==21:
                count4[14]+=1
                sum_variance4[14]=sum_variance4[14]+varianceDict[key]
            elif hr==22:
                count4[15]+=1
                sum_variance4[15]=sum_variance4[15]+varianceDict[key]
            elif hr==23:
                count4[16]+=1
                sum_variance4[16]=sum_variance4[16]+varianceDict[key]
    cnt=0
    listM=[]
    listTu=[]
    listW=[]
    listTh=[]
    for i in sum_variance1:
        if count1[cnt]==0:
            count1[cnt]+=1
        if count2[cnt]==0:
            count2[cnt]+=1
        if count3[cnt]==0:
            count3[cnt]+=1
        if count4[cnt]==0:
            count4[cnt]+=1
        avg_variance1=float(sum_variance1[cnt])/count1[cnt]
        avg_variance2=float(sum_variance2[cnt])/count2[cnt]
        avg_variance3=float(sum_variance3[cnt])/count3[cnt]
        avg_variance4=float(sum_variance4[cnt])/count4[cnt]
        listM.append(avg_variance1)
        listTu.append(avg_variance2)
        listW.append(avg_variance3)
        listTh.append(avg_variance4)
        """
        print "----------------------------------------"
        #print "The total number of instances of time slot(",(cnt+7),"-",(cnt+8),")hrs is=",count[cnt]
        print "The average variance on Monday for ",routeName," in time-slot(",(cnt+7),"-",(cnt+8),")hrs is=",avg_variance1
        print "The average variance on Tuesday for ",routeName," in time-slot(",(cnt+7),"-",(cnt+8),")hrs is=",avg_variance2
        print "The average variance on Wednesday for ",routeName," in time-slot(",(cnt+7),"-",(cnt+8),")hrs is=",avg_variance3
        print "The average variance on Thursday for ",routeName," in time-slot(",(cnt+7),"-",(cnt+8),")hrs is=",avg_variance4
        """
        cnt+=1
    print "Monday variance list :" , listM
    print "Tuesday variance list :" , listTu
    print "Wednesday variance list :" , listW
    print "Thursday variance list :" , listTh
    return






#GroupBy("RouteA","","",True,True)
#GroupBy("RouteX","","",True,False)
GroupBy("RouteX","","",False,True)
#GroupByTimeofDay("RouteA")
#calculateVariance("RouteE","","")
#GroupByDayofWeek("RouteX")
#calculateClosest("RouteE")
#populateSchedule("RouteB")
