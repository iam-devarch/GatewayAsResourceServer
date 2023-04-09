# Setup Project Infra
In our case, we’ll use the Keycloak identity provider. We have to set up following things in keycloak after installation.

1. Start Keyclooak server locally 
![img_8.png](README_resources/img_8.png)

2. Create Realm called _myrealm_
![img.png](README_resources/img.png)

3. Create Client named _oauth-sample-client_
![img_2.png](README_resources/img_2.png)

4. Create one user under this realm
![img_1.png](README_resources/img_1.png)

# Get hold of _access_token_ to authorize resources under this gateway
1. call keycloak endpoint _/realms/myrealm/protocol/openid-connect/auth_ with query params given below. It'll prompt for credentials, and you'll have to key-in username and password created under keycloak (Step 3 under _Setup Project Infra_ section).
![img_3.png](README_resources/img_3.png)

2. You'll get code as part of query params in the redirected url after you sign in
![img_4.png](README_resources/img_4.png)

3. Use this code to get _access_token_ from keycloak endpoint _/realms/myrealm/protocol/openid-connect/token_
![img_5.png](README_resources/img_5.png)

4. First send it without Authorization header and it'll fail.
![img_7.png](README_resources/img_7.png)

5. It'll be successfully authorized to redirect after you use returned access_token in the header _Authorization_ with prefix _Bearer_ to call endpoint under this api gateway (resource server).
Ignore the error as our gateway is redirecting it to the server which is not running currently.
![img_6.png](README_resources/img_6.png)

