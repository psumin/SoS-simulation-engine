[![License: Apache-2.0](https://img.shields.io/badge/License-Apache2.0-yellow.svg)](https://opensource.org/licenses/Apache-2.0)

# Dynamic SIMVA-SoS

Dynamic SIMVA-SoS is a dynamic simulation-based verification and analysis tool for system of systems. Dynamic simulation means that the user can stop the simulation and inject the new scenario events while simulation is executing. To support these features, we adopted discrete time multi-agent simulation structure. Discrete time multi-agent simulation is a good structure to controll the injected events simultaneously. Also, it focuses only for the agents, not the whole system, and that provides a proper level of abstraction for reducing the complexity of simulation. Dynamic SIMVA-SoS simulates a system of systems with injected scenarios, and performs statistical model checking to verify the achievement of SoS-level goals when simulation ends. SoS-level goals can be translated into properties of the SoS.

# Installation Guide

[Preparation]

You need to install following environment to run our Dynamic SIMVA-SoS:

● JVM 1.8 or later
● Gradle 3.1 or later
We recommend that you install IntelliJ to run our tool easier.

[Process]

Checkout our Dynamic SIMVA-SoS where you want it installed.
$ git clone https://github.com/sumin0407/SoS-simulation-engine.git
Please import the stored libraries in project structure. These libraries are used for writing logs in Excel file.

If the modules are not considered as source code, please apply the src as source code in projects structure

# User Manual
0. Modify the simulation end condition (frame number), choose the property to check.
1. Run the program in IntelliJ by running main.java.
2. Press "s" if you want to interrupt the simulation
3. When the simulation is stopped, follow the console's command.
4. Resume the simulation.
5. When simulation ends, statistical model checking (SMC) performs. When SMC is done, check the result.


# More Information

Previously, we implemented SIMVA-SoS (SIMulation-based Verification and Analysis tool for System of Systems). We are sorry to tell you that some of the related publications are written in Korean. If you have any questions or suggesitions, please email us, [smpark@se.kaist.ac.kr](mailto:smpark@se.kaist.ac.kr). We are pleased to have talks with you.

https://github.com/SESoS/SIMVA-SoS

[Mingyu Jin, Donghwan Shin, Junho Kim, Doo-Hwan Bae. "System-of-Systems-level Goal Achievement Verification Tool." Proceedings of KIISE,  (2017.6): 552-554.](http://www.dbpia.co.kr/Journal/ArticleDetail/NODE07207302)

[Junho Kim, Donghwan Shin, Doo-Hwan Bae. "An Applicability Study of Action-Benefit-Cost Model and Statistical Model Checking for System of Systems Goal Achievement Verification." KIISE Transactions on Computing Practices, 23.4 (2017.4): 256-261.](http://www.dbpia.co.kr/Journal/ArticleDetail/NODE07153954)

[Park Sumin, Zelalem Mihret Belay, and Doo-Hwan Bae. "A simulation-based behavior analysis for MCI response system of systems." 2019 IEEE/ACM 7th International Workshop on Software Engineering for Systems-of-Systems (SESoS) and 13th Workshop on Distributed Software Development, Software Ecosystems and Systems-of-Systems (WDES). IEEE, 2019.](https://ieeexplore.ieee.org/abstract/document/8882856)


## Copyright

Copyright 2017 - SESoS Group in Korea Advanced Institute of Science and Technology (KAIST).
