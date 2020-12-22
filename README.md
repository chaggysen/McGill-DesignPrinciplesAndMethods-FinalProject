# ECSE211 Project: Team 10 - DORAEMON

## Overview:
<img width="1440" alt="UserAndEmailDemo" src="https://github.com/chaggysen/McGill-DesignPrinciplesAndMethods-FinalProject/blob/master/ExampleMap.png">
The goal of this project is to design and construct a machine that can autonomously navigate a closed course to an island, search for a set of containers, and deposit as many of these as possible into a bin as shown in Figure 1 – all within a prescribed time limit.

## The playing field:
The playing field measures 15’ x 9’, with the origin located in the lower left hand corner, (0,0), as shown in Figure 1. There are three zones. Each of the zones is surrounded by a virtual river (blue regions), connected to a central island (Yellow Zone) by covered bridges. Each zone corresponds to a rectangular region defined by its lower left (LL) and upper right (UR) corners relative to the origin. In the example shown in Figure 1, the Red Zone is defined as Red_LL (0,5) to Red_UR (4,9), and the green zone is defined as Green_LL (10,0) to Green_UR (15,4). Each corner of the playing field is labeled Corner 0, 1, 2, 3. Note that the layout of the competition area is not fixed (Figure 1 is an example only). Information about the field is transmitted to each machine/player using a provided WiFi class. In the Wifi dialog, the coordinates defining the zones are passed as individual components, hence Red_LL (x,y) would be sent as Red_LL_x and Red_LL_y.

## The rules & tasks:
Consider the scenario depicted in Figure 1, with two players labeled R1 and R3. The labels indicate the corners each machine started in, so Player 1 starts in Corner 1 (Green Zone) and Player 3 in Corner 3 (Red Zone). At the start of a round, both players are placed in their respective corners at a random orientation and started. Each player waits for a set of game parameters to be downloaded from the game server (more about this later). Once the parameters are received (which describe the layout of the playing field), each player must cross the river over to the central island by crossing the bridges (you cannot pretend that your robots can swim!). The key parameters here are RedTeam, GreenTeam, RedCorner and GreenCorner. Each player has an assigned team number, so it can determine whether it is in the red corner or green corner by matching against RedTeam or GreenTeam. Once the team color is identified, the starting corner can be located by the RedCorner and GreenCorner parameters respectively. From here a key landmark becomes available, i.e. the location of the bridge connecting the starting zone to the island. In the example shown in Figure 1, the red player would cross using the bridge located at TNR_LL (4,7) to TNR_UR (6,8). Once on the island, the player proceeds to a search zone (a rectangular area on the island) in which there are several containers. As shown in Figure 1, the search zone for the red player is located at SZR_LL (6,5) to SZR_UR (10,9). The player then proceeds to locate and push containers up the ramp into a bin. As shown, the ramp for the red player is located by two parameters, RRL (9,7) and RRR (10,7), from which position and orientation can be identified. Each of the containers has a different weight so the score for pushing N containers into the bin is ∑ 𝐶𝐶𝑖𝑖 𝑁𝑁 𝑖𝑖=1 𝑊𝑊𝑖𝑖, where Wi is the weight associated with container Ci. Given the time limitation, it might not be possible to locate all the containers in the search zone, so your search strategy should include determining weight by measuring motor torque as per Lab 5. A .wbt file containing the layout in Figure 1 (without robots) will be posted to the Project folder on myCourses shortly. An update to this document will contain a table listing the points associated with each weight class. As part of the Sensor Characterization exercise, which is generally performed during the first phase of the project, you will determine the motor torques associated with each weight classes so that they can be readily identified. Once the containers are deposited (or you decide to cease this task because the time you allocated for completion has been reached), the robot makes its way back to the initial starting position. Similarly, the green player would cross using the bridge located at TNG_LL (10,3) to TNG_UR (11,5), and proceeds to its search zone located at SZG_LL (11,5) to SZG_UR (15,9). The ramp for the green player is located at coordinates GRL(12,7) and GRR(13,7). There are two complications that each player must cope with. First, there are obstacles located at random positions on the island. Second, both players must avoid hitting each other. The first player to contact the second is automatically disqualified.
Pay close attention to how the bridges are positioned relative to the Red and Green zones. Notice, in this example, that the bridge connecting the Red zone joins at the boundary whereas the bridge connecting the Green zone overlaps by one square. This will always be the case when the border separating two zones is one square wide. As part of the design of the player machine, it will be required to fit through the covered bridge. Since there is a time limit (which will be announced after the results of the Beta demo are in), machines must be nimble enough to move with a reasonable speed. In starting your design, you can assume that the nominal time limit is 5 minutes from receipt of parameters to completion of the task. If this time is changed, it will be
adjusted upwards (more time).

*DISCLAIMER*    
These are not perfect projects and shouldn't be copied.     
McGill University's Engineering Faculty uses code-plagiarism software to detect any copied code.
