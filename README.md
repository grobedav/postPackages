# Post Delivery Packages
Write a command line program that keeps a record of packages processed. Each package information consists of weight (in kg) and destination postal code. Think about these packages in the same way, when you send one using postal office. Data should be kept in memory (please don’t introduce any database engines). 

The program should:

read user input from console, user enters line consisting of weight of package and destination postal code
once per minute - write output to console, each line consists of postal code and total weight of all packages for that postal code

process user command “quit”, when user enters quit to command line as input, program should exit
take and process command line argument specified at program run – filename of file containing lines in same format as user can enter in command line. This is considered as initial load of package information
handle invalid input of user (it is up to you how, describe implemented behaviour in readme file)

Sample input: 

* 3.4 08801
* 2 90005
* 12.56 08801
* 5.5 08079 
* 3.2 09300

Input line format:
<weight: positive number, >0, maximal 3 decimal places, . (dot) as decimal separator><space><postal code: fixed 5 digits> 

Sample output (order by total weight): 

* 08801 15.960
* 08079 5.500
* 09300 3.200
* 90005 2.000

 Output line format:
<postal code: fixed 5 digits><space><total weight: fixed 3 decimal places, . (dot) as decimal separator>

Things you may consider using: 

* Unit testing 
* Documentation 
* Programming patterns 
* Thread safety 

 Please put your code in a bitbucket/github repository. We should be able to build and run your program easily (you may wish to use Maven, Ant, etc). Include instructions on how to run your program. 

 

Optional bonus task

The program should:
take and process another command line argument specified at program run – filename of file containing information about fees related to package weight. Once such file is specified as argument then output written to console will contain also total fee for packages sent to certain postal code, see format of file and out here after

Format of file containing fees (sample):

* 10 5.00
* 5 2.50
* 3 2.00
* 2 1.50
* 1 1.00
* 0.5 0.70
* 0.2 0.50

Line format:

<weight: positive number, >0, maximal 3 decimal places, . (dot) as decimal separator><space><fee: positive number, >=0, fixed two decimals, . (dot) as decimal separator> 

Meaning:

delivery fee of package weighing more than or exactly 10 (kg) is 5.00 (Eur),
delivery fee of package weighing more than or exactly 5 (kg) and less than 10 (kg) is 2.50 (Eur), etc.

 

Sample output: 

* 08801 15.960 7.00
* 08079 5.500 2.50
* 09300 3.200 2.00
* 90005 2.000 1.50

Output line format:

<postal code: fixed 5 digits><space><total weight: fixed 3 decimal places, . (dot) as decimal separator><space><total fee: fixed 2 decimal places, . (dot) as decimal separator)

## Run
    mvn clean package
    cd target
    java -jar postPackages-1.0-SNAPSHOT.jar
## Documentation
* This file
* self-describing methods

The solution, what I created is fulfilling all requirements, also the bonus one. I created this project from maven quick-start archetype. I defined in *pom.xml* file main class, so the project can be quickly builded and simply run. The project is packaged as one jar file. I little bit change requirements for inputs. All inputs have upper range limit. You can add only values in range (0 - 999>. As a programmer I belong to the group which prefer simple self-describing methods. So whole solution is created in this spirit without the spaghetti code. Business logic is in the *Service* and can be reused for any other canals (REST). Main class *App* reads only inputs from console and there is no touch to the core logic. Also *WriteTask* is used only for the visibility of outputs. Inputs are validated in *Service* against some regex pattern and throw *RunTimeException* if is there any validation problem, the error message is sent to console and user can enter new right values. User can start with definition of any package weight with zipCode without fees file.  Fees file parameter can be added during insertion of values, even file himself can be changed (updated) any time and enter again as third parameter. File inputs are also validated and if any of inputs is wrong all readed values from file will be deleted and program will be waiting for new updated file in the good shape.

## Unit Testing
Implementation is covered by unit tests. For regex patterns is created parametrized unit test class and for other business logic is created normal unit test class.

## Programming patterns
I am using read-write concurrency pattern.

## Thread safety
I decided to use standard java conccurency objects with synchronized blocks. I am also using for writing periodically outputs and safe shutdown *ExecutorService*.
