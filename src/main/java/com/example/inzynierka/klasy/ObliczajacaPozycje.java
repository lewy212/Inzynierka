package com.example.inzynierka.klasy;

import org.graphstream.graph.Node;
import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Point3;

public class ObliczajacaPozycje {
    public static void nodePosition(Node node, double[] xyz) {
        if (xyz.length >= 3) {
            if (!node.hasAttribute("xyz") && !node.hasAttribute("xy")) {
                if (node.hasAttribute("x")) {
                    xyz[0] = node.getNumber("x");
                    if (node.hasAttribute("y")) {
                        xyz[1] = node.getNumber("y");
                    }

                    if (node.hasAttribute("z")) {
                        xyz[2] = node.getNumber("z");
                    }
                }
            } else {
                Object o = node.getAttribute("xyz");
                if (o == null) {
                    o = node.getAttribute("xy");
                }

                if (o != null) {
                    positionFromObject(o, xyz);
                }
            }

        }
    }

    private static void positionFromObject(Object o, double[] xyz) {
        if (o instanceof Object[]) {
            Object[] oo = (Object[])o;
            if (oo.length > 0 && oo[0] instanceof Number) {
                xyz[0] = ((Number)oo[0]).doubleValue();
                if (oo.length > 1) {
                    xyz[1] = ((Number)oo[1]).doubleValue();
                }

                if (oo.length > 2) {
                    xyz[2] = ((Number)oo[2]).doubleValue();
                }
            }
        } else if (o instanceof Double[]) {
            Double[] oo = (Double[])o;
            if (oo.length > 0) {
                xyz[0] = oo[0];
            }

            if (oo.length > 1) {
                xyz[1] = oo[1];
            }

            if (oo.length > 2) {
                xyz[2] = oo[2];
            }
        } else if (o instanceof Float[]) {
            Float[] oo = (Float[])o;
            if (oo.length > 0) {
                xyz[0] = (double)oo[0];
            }

            if (oo.length > 1) {
                xyz[1] = (double)oo[1];
            }

            if (oo.length > 2) {
                xyz[2] = (double)oo[2];
            }
        } else if (o instanceof Integer[]) {
            Integer[] oo = (Integer[])o;
            if (oo.length > 0) {
                xyz[0] = (double)oo[0];
            }

            if (oo.length > 1) {
                xyz[1] = (double)oo[1];
            }

            if (oo.length > 2) {
                xyz[2] = (double)oo[2];
            }
        } else if (o instanceof double[]) {
            double[] oo = (double[])o;
            if (oo.length > 0) {
                xyz[0] = oo[0];
            }

            if (oo.length > 1) {
                xyz[1] = oo[1];
            }

            if (oo.length > 2) {
                xyz[2] = oo[2];
            }
        } else if (o instanceof float[]) {
            float[] oo = (float[])o;
            if (oo.length > 0) {
                xyz[0] = (double)oo[0];
            }

            if (oo.length > 1) {
                xyz[1] = (double)oo[1];
            }

            if (oo.length > 2) {
                xyz[2] = (double)oo[2];
            }
        } else if (o instanceof int[]) {
            int[] oo = (int[])o;
            if (oo.length > 0) {
                xyz[0] = (double)oo[0];
            }

            if (oo.length > 1) {
                xyz[1] = (double)oo[1];
            }

            if (oo.length > 2) {
                xyz[2] = (double)oo[2];
            }
        } else if (o instanceof Number[]) {
            Number[] oo = (Number[])o;
            if (oo.length > 0) {
                xyz[0] = oo[0].doubleValue();
            }

            if (oo.length > 1) {
                xyz[1] = oo[1].doubleValue();
            }

            if (oo.length > 2) {
                xyz[2] = oo[2].doubleValue();
            }
        } else if (o instanceof Point3) {
            Point3 oo = (Point3)o;
            xyz[0] = oo.x;
            xyz[1] = oo.y;
            xyz[2] = oo.z;
        } else if (o instanceof Point2) {
            Point2 oo = (Point2)o;
            xyz[0] = oo.x;
            xyz[1] = oo.y;
            xyz[2] = 0.0;
        } else {

        }

    }
}
