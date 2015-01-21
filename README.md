# capstone

My Symptom Management Capstone application and service source code.

Demonstration video can be found at: https://www.youtube.com/watch?v=m7tyCbfReXk

First import the CapstoneProject as a gradle project. Right click in eclipse import. Select gradle project and browse to the CapstoneProject. After that build model and then click finish.

Second import the SymptomManager as Existing Android Code Into Workspace. Browse to SymptomManager folder and click finish

Third import the google-play-services_lib as Existing Android Code Into Workspace. Browse to google-play-services_lib folder and click finish.

Forth import the MPChartLib as Existing Android Code Into Workspace. Browse to MPChartLib folder and click finish.

Fifth right click on SymptomManager application in eclipse -> Properties. Remove the Library references which will be with X red mark. Click Add, select the google-play-services_lib project from the window and click OK. Then click Add, select the MPChartLib project from the windows and click OK. Everything should be build OK now!

In eclipse open the tab Run -> Select the Run Configurations… -> there select Java Application from the list and Create New Launch Configuration from the first icon in the left of the window or right click select New. There in the tab Main (the first one) Browse the project select CapstoneProject and in the Main class add the Application class that contains the main method

Then in the Arguments tab (the second one next to the Main tab) in the VM arguments add the following:
-Dkeystore.file=src/main/resources/private/keystore2.jks -Dkeystore.pass=courser
Here we are declaring the path of the keystore inside the project and the password for the keystore.

Then select run and the server will execute. 


