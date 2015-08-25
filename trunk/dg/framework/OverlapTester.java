package com.tfu.dg.framework;


public class OverlapTester {
	public static boolean overlapSpheres (Sphere s1, Sphere s2) {
		float distance = s1.center.dst2(s2.center);
		float radiusSum = s1.radius + s2.radius;
		return distance <= radiusSum * radiusSum;
	}
}
