import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
    url: "http://localhost:8081",
    realm: "library",
    clientId: "library-frontend",
});

export default keycloak;
