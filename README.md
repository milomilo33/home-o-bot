# home-o-bot
University cybersecurity course project.

The project focuses on creating a home security system for monitoring and securing properties, including apartments, cottages, and houses.
The system aimed to detect and alert users about unexpected events, such as power outages, break-ins, and interior changes.

# Architecture and features

The architecture of the system consists of the central app (as separate backend _Spring Boot_ and frontend _Vue.js_ apps) and, on the other hand, _devices_.

The central app:
- Collects, normalizes, and filters events from various devices to detect and alert users about changes. It uses a rule-based system (_Drools_) to correlate events over time and trigger alarms for unexpected occurrences.
- Recognizes four types of users: unregistered users, admins, property owners and property renters.
- Unregistered users can only create _certificate signing requests_ (_CSRs_).
- Admins are central to system security and have five main responsibilities:
  - Public Key Infrastructure (_PKI_): certificate creation, revocation, distribution etc.
  - User management within the system
  - Configuration of properties and devices
  - Alarm definition and tracking
  - Viewing/searching logs
- Owners can assign renters to their properties, check their devices and read the devices' messages, as well as get reports of device activity in certain periods.
- Renters have similar features to owners, except for the assignment of properties.

Devices:
- Monitor different environmental signals, such as door openings, light changes, and temperature shifts.
- Generated standardized messages enriched with relevant information for the central app.
- Randomly alternate between normal and attack states in their messages for testing.
- Represented as scripts written in _Python_.

Throughout the system the following are also taken into account and/or implemented:
- Protection of sensitive data during storage, transport, and usage.
- Encryption or hashing of unavoidable data storage.
- Secure HTTPS communication configuration.
- Digital signatures for exchanged messages (e.g. between devices and the central app).
- Authentication and authorization mechanisms.
- Role-based access control (RBAC) for access control.
- OWASP Top 10 security risks.

# How to run (example use case)
1. Run the central [app](https://github.com/milomilo33/home-o-bot/blob/main/src/main/java/com/robot/homeobot/HomeOBotApplication.java) (backend).
2. Run the frontend application for the central app via ```npm install``` & ```npm run serve``` within the _./src/main/ui/vue_ project directory.
3. Open [localhost:8080](http://localhost:8080/).
4. Create a _CSR_ for each device you want to add/use within the system (available device scripts are located in _./devices_, where the name of the script is the name of the device, which should be the common name within a _CSR_). Example: ![image](https://github.com/milomilo33/home-o-bot/assets/29868001/115ce339-6961-4bfc-a2f2-b62ac5532b00)
5. Log in as an admin (there are example preset accounts for all types of users; all passwords are 123, and usernames are _admin_ for the example admin, _owner1_ for the example owner and _renter1_ for the example renter).
6. On the _All CSRs_ page, generate the certificates for each _CSR_.
7. Move (distribute) the generated private keys (e.g. _door-1.key_) to devices from the _./store/private_keys_to_distribute_ project directory to _./devices_.
8. Run the corresponding _Python_ device scripts in _./devices_ (e.g. _door-1.py_) for the devices to start generating signed messages.
9. From then on, use the rest of the features of the system by navigating within the central frontend application.
