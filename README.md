#### Title: Traceability System for Supply Chain Based on Blockchain Technology    

### Development Environments & Tools   
* Blockchain Network: VMware, Ubuntu 22.04.1 LTS, Docker 20.10.18, Hyperledger Fabric, Hyperledger Explorer, Minifabric  
* Back End: IntelliJ IDEA Ultimate, JDK 16.0.1, Spring Boot, Redis, MySQL, Apache Maven 3.8.1  
* Front End: WebStorm, Vue.js 3.2.39, Node.js 16.16.0  
   
### Project Folder Structure     
* system-backend: system back-end part  
    * blockchain-gateway: back-end blockchain gateway containing all the implemented blockchain-based APIs  
    * common-resource: common resources served for the back-end part
    * server-framework: back-end fixed configurations and models  
    * server-interface: back-end normal server containing all the implemented common logic APIs  
* system-blockchain: system blockchain part  
    * blockchain-chaincode: blockchain chaincode installed within the blockchain network  
    * blockchain-network: blockchain network configuration files, certificates and private keys used for connection  
* system-design: system diagrams in the design work  
* system-frontend: system front-end part containing components and user views  
  
### Running Instructions   
* Prerequisite Programs Required to be Running:  
    Hyperledger Fabric Blockchain Network, Hyperledger Explorer, Redis, MySQL, Node.js  
* Guidance on building blockchain network and explorer:  
    https://hyperledger-fabric.readthedocs.io/en/latest/getting_started.html  
    https://github.com/hyperledger-labs/minifabric/blob/main/docs/README.md  
* Copy the generated folder **keyfiles**, folder **wallet**, and connection file into the **lxl114/system-backend/blockchain-gateway/src/main/resources**. Also, modify the file **application.yml** under this path and install the module **blockchain-chaincode** within the running blockchain network.  
* Prepare the account data stored in MySQL following the RBAC related to the user, role, and permission. Then, modify the Redis and MySQL connection configurations in **application-datasource.yml** and **application-framework.yml** under the path **lxl114/system-backend/server-framework/src/main/resources**.  
* Run the starter classes **BlockchainGatewayApplication (port: 9001)** and **NormalServerApplication (port: 9000)** in modules **blockchain-gateway** and **server-interface**, respectively. Then use Node.js to run the front-end module **system-frontend (port: 8080)**.  
* Jump to the login page through the URL http://localhost:8080/login.  





