FROM open-liberty:microProfile2-java8-ibm

COPY openliberty/server.xml /opt/ol/wlp/usr/servers/defaultServer/server.xml
COPY target/liberty/wlp/usr/extension /opt/ol/wlp/usr/extension/

COPY target/dbwrapper.war /opt/ol/wlp/usr/servers/defaultServer/dropins/
