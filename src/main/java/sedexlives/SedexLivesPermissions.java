/*
MIT License

Copyright (c) 2020 Steinein_

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package sedexlives;

public class SedexLivesPermissions {

    // Holds all permissions here for easier checking.

    /*
    When adding new permission, follow the format:

    public static final String PERMISSION_NAME = "permission.string" // Permission description
     */

    public static final String RELOAD_CONFIG = "sedexlives.reload"; // Lets the player reload the config.
    public static final String USE_LIVES = "sedexlives.use"; // Lets the player use the lives system.
    public static final String KEEP_EXP = "sedexlives.keepxp"; // Lets the player keep their XP on death.
    public static final String CHECK_LIVES = "sedexlives.check"; // Lets the player check their own lives.
    public static final String CHECK_LIVES_OTHERS = "sedexlives.check.others"; // Lets the player check others' lives.
    public static final String CHECK_MAXLIVES = "sedexlives.check.maxlives";
    public static final String CHECK_MAXLIVES_OTHERS = "sedexlives.check.maxlives.others";
    public static final String SET_LIVES = "sedexlives.set"; // Lets the player set others' lives.

}
