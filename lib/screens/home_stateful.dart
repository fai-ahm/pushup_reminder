import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:shared_preferences/shared_preferences.dart';

class HomeStateful extends StatefulWidget {
  @override
  _HomeStatefulState createState() => _HomeStatefulState();
}

class _HomeStatefulState extends State<HomeStateful> {
  DateTime dateNow = DateTime.now();
  var _onPressed;
  var _startTodayBtnColor;
  bool timerStatusGlobal;
  int pushupCounter;

  @override
  void initState() {
    _getTimerStatusFromSharedPrefs();
    _getCounterFromSharedPrefs();

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text('Push Up Reminder'),
      ),
      body: Column(
        children: <Widget>[
          Padding(
            padding: EdgeInsets.only(top: 15.0),
          ),
          todaydate(),
          Center(
            child: Padding(
              padding: const EdgeInsets.only(top: 80.0),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Ink(
                      decoration: ShapeDecoration(
                          shape: CircleBorder(), color: Colors.grey[700]),
                      child: IconButton(
                        icon: Icon(Icons.remove),
                        onPressed: () {
                          _decrementCounter();
                          debugPrint('minus pressed');
                        },
                      ),
                    ),
                  ),
                  Text(
                    '$pushupCounter',
                    style:
                        TextStyle(fontSize: 120.0, fontWeight: FontWeight.w700),
                  ),
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Ink(
                      decoration: ShapeDecoration(
                          shape: CircleBorder(), color: Colors.grey[700]),
                      child: IconButton(
                        icon: Icon(Icons.add),
                        onPressed: () {
                          _incrementCounter();
                          debugPrint('plus pressed');
                        },
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
          Text('Pushup counter'),
          Text('Please only increment one every week'),
        ],
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
      floatingActionButton: FloatingActionButton.extended(
        elevation: 8.0,
        backgroundColor: _startTodayBtnColor,
        icon: Icon(Icons.fitness_center),
        label: Text(
          'START TODAY',
          style: TextStyle(fontSize: 20.0, fontWeight: FontWeight.w700),
        ),
        onPressed: _onPressed,
      ),
    );
  }

  //increases the counter by 1
  void _incrementCounter() {
    if (pushupCounter >= 50) {
      debugPrint('pushup counter cannor be more than 50');
    } else {
      setState(() {
        pushupCounter++;
        _setCounterInSharedPrefs(pushupCounter);
      });
    }
  }

  //decreases the counter by 1
  void _decrementCounter() {
    if (pushupCounter <= 3) {
      setState(() {
        debugPrint('pushup counter cannor be less than 3');
      });
    } else {
      setState(() {
        pushupCounter--;
        _setCounterInSharedPrefs(pushupCounter);
      });
    }
  }

  Future<void> _setCounterInSharedPrefs(int pushupCounter) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setInt("pushupCounter", pushupCounter);
  }

  Future<void> _getCounterFromSharedPrefs() async {
    final prefs = await SharedPreferences.getInstance();
    pushupCounter = prefs.getInt("pushupCounter");
    if (pushupCounter == null) {
      pushupCounter = 3;
    }
  }

  //will get timer status from shared preferences in a bollean which will be true if it is running
  //will return false if not running
  Future<bool> _getTimerStatusFromSharedPrefs() async {
    final prefs = await SharedPreferences.getInstance();
    final timerStatus = prefs.getBool("timerStatus");
    debugPrint('timer status is $timerStatus');
    if (timerStatus == null || timerStatus == false) {
      setState(() {
        timerStatusGlobal = false;
      });

      return false;
    } else {
      setState(() {
        timerStatusGlobal = true;
      });
      return timerStatus;
    }
  }

  //set the timer status to running will turn it to true when the button is pressed
  Future<void> _setTimerStatusInSharedPrefs() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setBool("timerStatus", true);
    setState(() {
      timerStatusGlobal = true;
    });
  }

  //increment the pushup counter and then displays it also checks if it is above the 50 limit

  todaydate() {
    DateTime dateNow = DateTime.now();
    int dayOfWeek = dateNow.weekday;

    String dayName;
    String pushups;

    switch (dayOfWeek) {
      case 1:
        dayName = 'Monday';
        pushups = 'No, you don\'t have to do push ups today';

        break;
      case 2:
        dayName = 'Tuesday';
        pushups = 'Yes, you have to do push ups today';
        break;
      case 3:
        dayName = 'Wednesday';
        pushups = 'No, you don\'t have to do push ups today';
        break;
      case 4:
        dayName = 'Thursday';
        pushups = 'Yes, you have to do push ups today';
        break;
      case 5:
        dayName = 'Friday';
        pushups = 'No, you don\'t have to do push ups today';
        break;
      case 6:
        dayName = 'Saturday';
        pushups = 'Yes, you have to do push ups today';
        break;
      case 7:
        dayName = 'Sunday';
        pushups = 'No, you don\'t have to do push ups today';
        break;
    }
    if (pushups == 'Yes, you have to do push ups today' &&
        timerStatusGlobal == false) {
      _onPressed = () {
        setAlarmAndNotificationsInAndroid(dateNow);
        debugPrint('Start Service button is clicked');
      };
      _startTodayBtnColor = null;
    } else {
      _startTodayBtnColor = Colors.grey;
      _onPressed = null;
    }
    return Column(
      children: <Widget>[
        Center(
          child: Text(
            '$dayName',
            style: TextStyle(fontSize: 30.0, fontWeight: FontWeight.w700),
          ),
        ),
        Center(
          child: Text(
            '$pushups',
            style: TextStyle(fontSize: 20.0, fontWeight: FontWeight.w400),
          ),
        ),
      ],
    );
  }

  void setAlarmAndNotificationsInAndroid(dateNow) async {
    int endingTime = 23;
    DateTime time =
        DateTime(dateNow.year, dateNow.month, dateNow.day, endingTime, 0);
    Duration timeRange = time.difference(dateNow);
    debugPrint('time Range = $timeRange');

    // int timePeriod1 = timeRange.inMinutes ~/ 10;
    // debugPrint('time Period = $timePeriod1 minutes');

    int timePeriod = (timeRange.inMinutes ~/ 10) - 1;
    debugPrint('time Period = $timePeriod minutes');

    if (Platform.isAndroid) {
      MethodChannel _methodChannel =
          MethodChannel('com.pushup_reminder.messages');
      String data =
          await _methodChannel.invokeMethod('startService', timePeriod);

      debugPrint('this is the data $data');

      _setTimerStatusInSharedPrefs();
      debugPrint('timer status is set to true');
    }
  }
}
