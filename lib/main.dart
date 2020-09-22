import 'package:flutter/material.dart';
import 'package:pushup_reminder/screens/home_stateful.dart';

main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Push Up Reminder',
      theme: ThemeData(
        brightness: Brightness.dark,
        primaryColor: Colors.red[700],
        accentColor: Colors.redAccent,
      ),
      home: HomeStateful(),
    );
  }
}
