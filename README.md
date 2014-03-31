# Overview
Yahoo Connected TV’s Device Communication model enables new and exciting TV-viewing experiences driven by the sophisticated controls available on today’s tablets, laptops, and mobile phones. A new generation of TV App capabilities is now available to consumers, including gesture-based, multi-display, and multi-user features for intensive gaming, multimedia, and social applications. 

This Device Communication platform supports two-way message-passing between Internet-enabled devices and connected TVs running the Yahoo Connected TV Platform. Keyboard, navigation, and app-specific messages can be communicated through a new protocol over a local network. 

The Yahoo Connected TV Device Communication Java Library uses several 3rd party open source libraries and tools. This file summarizes the tools used, their purpose, and the licenses under which they are released. This file also explains how to build the jar file and generate Javadoc documentation.

# Build
To create a jar for this library use Maven (http://maven.apache.org/). Use the command:

> mvn deploy

This will download all dependencies and create the jar file "target/device-communcation.jar".

# Documentation
If you wish to create documentation from the source use this command:

> mvn "javadoc:javadoc"

Generated documentation will be created in the doc/ folder. Open the doc/index.html file in your web browser.

You can find the Device Communication Tools repository at https://github.com/yahoo/ctv-devicecommunication-tools

You can find Device Communication documentation at https://developer.yahoo.com/connectedtv/devicecommunication 

You can find more information about Yahoo Connected TV at http://smarttv.yahoo.com and more developer documentation at https://developer.yahoo.com/connectedtv 

# Libraries
Except as specifically stated below, the 3rd party software packages are not distributed as part of this project, but instead are separately downloaded from their respective provider and built on the developer's machine prior to building the library.

* JmDNS version 3.4.0 (Apache 2.0 license)
* * (A mDNS client library)
* * http://jmdns.sourceforge.net/

* org.json version 20090211
* * (Java JSON Library)
* * http://json.org/java/

* For unit tests:
* * Junit version 4.8.2
* * * (Java testing library)
* * * http://junit.sourceforge.net/

* * Hamcrest version 1.3.RC2
* * * (Java unit test helper library)
* * * http://code.google.com/p/hamcrest/

# License
The Yahoo Connected TV Device Communication Java Library is licensed under the following BSD License.

Software License Agreement (BSD License)
Copyright (c) 2014, Yahoo. All rights reserved.

Redistribution and use of this software in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

* Neither the name of Yahoo. nor the names of Yahoo Connected TV's contributors may be used to endorse or promote products derived from this software without specific prior written permission of Yahoo.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
