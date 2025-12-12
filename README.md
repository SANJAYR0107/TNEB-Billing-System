TNEB Electricity Billing System (Java Console Project)

This is a simple Java console-based electricity billing system that calculates the electricity bill for domestic consumers based on TNEB slab rates.
The project supports consumer authentication, unit calculation, and generates a detailed bill summary.

🔹 Features

Login system using users.txt

Consumer name lookup from consumers.txt

Meter reading mode (previous & current reading)

Multiple appliance mode (AC, fan, TV, etc.)

TNEB slab-wise billing calculation

Fixed charge + electricity duty

Saves final bill to LastBill.txt

🔹 TNEB Tariff Slabs Used
Units Range	Rate
0–100	Free
101–400	₹4.70
401–500	₹6.30
501–600	₹8.40
601–800	₹9.45
801–1000	₹10.50
Above 1000	₹11.55
🔹 Sample Output
=========== TNEB BILL ===========
Consumer No     : 714023105044
Name            : Sanjay
Units Used      : 175.62
-----------------------------------------
Energy Charge   : ₹355.40
Fixed Charge    : ₹30.00
Electric Duty   : ₹17.77
-----------------------------------------
TOTAL BILL      : ₹403.16

🔹 Technologies Used

Java (Core)

File Handling

HashMap

Console Input/Output

🔹 Author

Sanjay R