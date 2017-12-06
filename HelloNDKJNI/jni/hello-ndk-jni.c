#include<jni.h>
#include<string.h>

//注意这里是又规则的

jstring Java_com_example_hellondkjni_MainActivity_stringFromNDKJNI( JNIEnv* env,jobject thiz )
{
	return (*env)->NewStringUTF(env, "Hello from NDK JNI !");
}
