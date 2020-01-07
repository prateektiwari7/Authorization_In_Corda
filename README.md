<p align="center">
  <img src="https://www.corda.net/wp-content/uploads/2016/11/fg005_corda_b.png" alt="Corda" width="500">
</p>

# Authorization in corda

This Project help you to create the User Account in Node and Authorized the user on the basis of password.

# Pre-Requisites

See https://docs.corda.net/getting-set-up.html.

# Usage


### Shell

When started via the command line, each node will display an interactive shell:

    Welcome to the Corda interactive shell.
    Useful commands include 'help' to see what is available, and 'bye' to shut down the node.
    
    Tue Nov 06 11:58:13 GMT 2018>>>

You can use this shell to interact with your node. For example, enter `run networkMapSnapshot` to see a list of 
the other nodes on the network:

    Tue Jan 07 15:23:22 IST 2020>>> run networkMapSnapshot
    - addresses:
         -"localhost:10008"
           legalIdentitiesAndCerts:
         -"O=PartyB, L=New York, C=US"
           platformVersion: 5
           serial: 1578390321582
    - addresses:
         -"localhost:10002"
           legalIdentitiesAndCerts:
         -"O=Notary, L=London, C=GB"
           platformVersion: 5
           serial: 1578390330531
    - addresses:
         -"localhost:10005"
           legalIdentitiesAndCerts:
         -"O=PartyA, L=London, C=GB"
           platformVersion: 5
           serial: 1578390329063

You can find out more about the node shell [here](https://docs.corda.net/shell.html).


### Create Key First

    Tue Jan 07 15:23:19 IST 2020>>> flow start CreateKey                                                                                          

    ✅   Starting
    ➡️   Done
    Flow completed with result: WNv3KutignVD6TdwSLbLDOfOLSztuFQr

### Create UserAcount on Party

    Tue Jan 07 15:19:15 IST 2020>>> flow start CreateAuthFlow accounttName: Prateek, toparty: PartyA, Authsecret: WNv3KutignVD6TdwSLbLDOfOLSztuFQr

     ✅   Starting
          Requesting signature by notary service
              Requesting signature by Notary service
              Validating response from Notary service
     ✅   Broadcasting transaction to participants
    ➡️   Done
    Flow completed with result: SignedTransaction(id=B3FF09C6BBBD317B5896576EE6EBE5413DB98DD055E765F157E4521330746365)
    
### Check the state data on Party

     Tue Jan 07 15:28:49 IST 2020>>> run vaultQuery contractStateType: com.template.states.AuthState
     states:
     - state:
         data: !<com.template.states.AuthState>
           Username: "Prateek"
           Authtoken: "dN2fEkH+vwmjYmlarozo5N3bD9AlFMOysXIMSPwC5WEpbyXD0cnmVy/I+ZmQIRdE"
           loginto: "O=PartyA, L=London, C=GB"
           linearId:
             externalId: null
             id: "700d0535-2348-4517-a10a-70317e6476f7"
         contract: "com.template.contracts.AuthContract"
         notary: "O=Notary, L=London, C=GB"
         encumbrance: null
         constraint: !<net.corda.core.contracts.SignatureAttachmentConstraint>
           key: "aSq9DsNNvGhYxYyqA9wd2eduEAZ5AXWgJTbTEw3G5d2maAq8vtLE4kZHgCs5jcB1N31cx1hpsLeqG2ngSysVHqcXhbNts6SkRWDaV7xNcr6MtcbufGUchxredBb6"
       ref:
         txhash: "B3FF09C6BBBD317B5896576EE6EBE5413DB98DD055E765F157E4521330746365"
         index: 0
     statesMetadata:
     - ref:
         txhash: "B3FF09C6BBBD317B5896576EE6EBE5413DB98DD055E765F157E4521330746365"
         index: 0
       contractStateClassName: "com.template.states.AuthState"
       recordedTime: "2020-01-07T09:50:33.893Z"
       consumedTime: null
       status: "UNCONSUMED"
       notary: "O=Notary, L=London, C=GB"
       lockId: null
       lockUpdateTime: null
       relevancyStatus: "RELEVANT"
       constraintInfo:
         constraint:
           key: "aSq9DsNNvGhYxYyqA9wd2eduEAZ5AXWgJTbTEw3G5d2maAq8vtLE4kZHgCs5jcB1N31cx1hpsLeqG2ngSysVHqcXhbNts6SkRWDaV7xNcr6MtcbufGUchxredBb6"
     totalStatesAvailable: -1
     stateTypes: "UNCONSUMED"
     otherResults: []
    
    
  Check that the above has Authtoken which is encrypted:-   
  
### Checking whether User is Authorised or not 

    
    Tue Jan 07 15:22:48 IST 2020>>> flow start Validation Username: Prateek, AuthSecret: WNv3KutignVD6TdwSLbLDOfOLSztuFQr

    ➡️   Starting
          Done
         You are validated user
  
### Trying to create same user with same Name

    Tue Jan 07 15:25:03 IST 2020>>> flow start CreateAuthFlow accounttName: Prateek, toparty: PartyA, Authsecret: mdx2ivd87jKwDbtnkz6Lq0eAZf2lolUC
    
    ➡️   Starting
    🚫   Done
    ☠   java.lang.IllegalArgumentException: There is already an account registered with the specified name Prateek.
    	☠   java.lang.IllegalArgumentException: There is already an account registered with the specified name Prateek.
  
  

  