# PiA-Light - Netaxept Android SDK v1.1.2-SNAPSHOT
----

PiA-Light Netaxept Android SDK is a light-weight version of the [Pia - Netaxept Android SDK](https://github.com/Nets-mobile-acceptance/Netaxept-Android-SDK). This version does not include the Card Scanner functionality (_CardIO_ Library is removed).

# Installation
----
+ Please add to the project's `gradle.properties` file the values for the following variables as provided by Nets

```gradle

## Required Maven URLs and Credentials ##
MAVEN_URL_PIA=provided value for URL to PiA SDK

MAVEN_REPO_USERNAME=providedUsername
MAVEN_REPO_PASSWORD=providedPassword

```

+ Please add code below to the project's `build.gradle` file.

```gradle
 allprojects {
        repositories {
            google()
            jcenter()

           maven {
                url MAVEN_URL_PIA
                credentials {
                    username MAVEN_REPO_USERNAME
                    password MAVEN_REPO_PASSWORD
                }
            }
        }
    }
	
```

+ In your `build.gradle` application level file, add:

```gradle

implementation('eu.nets.pia:pia-light-sdk:1.1.2-SNAPSHOT') { transitive = true; }

```

# Usage
---
Please check our [GitHub page](https://github.com/Nets-mobile-acceptance/Netaxept-Android-SDK) for more details related to new versions, documentation and best-practice code samples.

**Xamarin Support**: The only difference between the Official version and this one is that you need to include the [Light-Version DLL File](Xamarin) in your application. 
**ReactNative Support**: The only difference between the Official Version and this one is that in the `buid.gradle` file in your _Bridge_ you need to include the library from our Nexus private repository. Check **Installation** step for how to do it.

**Note:** Please have in mind that since _CardIO_ Library is removed from this version, all **UI Customization APIs** related to CardIO are no longer used. 

# Permissions
----
These permissions are handled inside the binary, and your integration won't require any additional changes.

**PiA SDK** will require the internet permissions to be fully operational.

```xml

<uses-permission android:name="android.permission.INTERNET" />

```

**CardIo** is no longer available, so _Camera Permission_ is no longer required.

# License
---

*****Copyright (c) 2020 Nets Denmark A/S*****


NETS DENMARK A/S, ("NETS"), FOR AND ON BEHALF OF ITSELF AND ITS SUBSIDIARIES AND AFFILIATES UNDER COMMON CONTROL, IS WILLING TO LICENSE THE SOFTWARE TO YOU ONLY UPON THE CONDITION THAT YOU ACCEPT ALL OF THE TERMS CONTAINED IN THIS BINARY CODE LICENSE AGREEMENT. BY USING THE SOFTWARE YOU ACKNOWLEDGE THAT YOU HAVE READ THE TERMS AND AGREE TO THEM. IF YOU ARE AGREEING TO THESE TERMS ON BEHALF OF A COMPANY OR OTHER LEGAL ENTITY, YOU REPRESENT THAT YOU HAVE THE LEGAL AUTHORITY TO BIND THE LEGAL ENTITY TO THESE TERMS. IF YOU DO NOT HAVE SUCH AUTHORITY, OR IF YOU DO NOT WISH TO BE BOUND BY THE TERMS, YOU MUST NOT USE THE SOFTWARE ON THIS SITE OR ANY OTHER MEDIA ON WHICH THE SOFTWARE IS CONTAINED.

Software is copyrighted. Title to Software and all associated intellectual property rights is retained by NETS and/or its licensors. Unless enforcement is prohibited by applicable law, you may not modify, decompile, or reverse engineer Software.

No right, title or interest in or to any trademark, service mark, logo or trade name of NETS or its licensors is granted under this Agreement.

Permission is hereby granted, to any person obtaining a copy of this software and associated documentation files ("the Software"), to deal in the Software, including without limitation the rights to use, copy, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: 

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

Software may only be used for commercial or production purpose together with Netaxept services provided from NETS, its subsidiaries or affiliates under common control.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.



