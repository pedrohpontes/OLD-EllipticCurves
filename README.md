# EllipticCurves
A library for dealing with elliptic and adding points in curves. Also includes implementations of some elliptic curves algorithms using that library.

# Description
EllCurve.java: the main class defining elliptic curves.
EllCurvePoint.java: implements the definition of a point in an elliptic curve and some arithmetic operations.
LenstrasFactorization.java: an implementation of Lenstra's algorithm (modified?) to try to factor an integer.

The 'main' methods offer an example of usage but no user interface (one must alter the code to alter the integer to be factored for example).

# Reference
All the algorithms are based on the following textbook:
Joseph H. Silverman, The Arithmetic of Elliptic Curves. Springer, 2nd Edition, 2009.
