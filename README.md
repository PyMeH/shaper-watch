# shaper-watch
The end goal we have is to generate an input for a CNC router to cut a beautiful wall watch, see end-goal.jpg
<img src="https://raw.githubusercontent.com/PyMeH/shaper-watch/master/end-goal.jpg" alt="end goal" width="200"/>


To calculate the placement of holes and cut lines we have a Java application that generates two .svg files - one for the Shaper Origin, and one for humans.
See result*.svg:
![result](https://raw.githubusercontent.com/PyMeH/shaper-watch/master/result-for-human.svg "for humans")

Finally, the result-for-shaper.svg file is passed to a Shaper Origin CNC to perform the cuts.
Smallest possible router bit is 6mm (see Constants#rHole)


Get a clock mechanism from AliExpress or [here](http://tpetrov.com/search.php?maincat=АКСЕСОАРИ&subcat=ЧАСОВНИЦИ%2C+ТЕРМОМЕТРИ&cat=ЧАСОВНИКОВИ+МЕХАНИЗМИ).

I bought black plastic washers (d=5mm D=10mm) and cut them where they overlap. Glued with black silicone to the board. 

Rope is added last (after sanding, washers, staining).

