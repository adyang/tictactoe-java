Tic-Tac-Toe in Java
====================

This is a toy project to practise writing extensible software using Test-Driven-Development (TDD).
It is inspired by 8th Light's example [apprenticeship program](http://dougbradbury.com/AgileAfrica.pdf).

The program was first written as a console program and later extended to include a GUI (using JavaFx).

Running Instructions
---------------------
* Tested on JDK 8
* For Windows, replace "./gradlew" with "./gradlew.bat".

1. To run Console game: ./gradlew run -PgameType=console --console=plain -q
2. To run GUI game: ./gradlew run -PgameType=gui -q
3. To run all tests: ./gradlew clean check