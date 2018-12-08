/*
 * Copyright (c) 2018 FTC team 4634 FROGbots
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.frogbots.ftcopmodetunercommon.misc;

import java.lang.reflect.Array;

public class CircularCache<T>
{
    private T[] cacheArray;
    int i = 0;

    public CircularCache(Class<T> t, int size)
    {
        // Use Array native method to create array
        // of a type only known at run time
        cacheArray = (T[]) Array.newInstance(t, size);
    }

    public void add(T obj)
    {
        cacheArray[i] = obj;
        i++;

        if(i >= cacheArray.length)
        {
            i = 0;
        }
    }

    public boolean contains(T obj2)
    {
        for(T obj1 : cacheArray)
        {
            if(obj1 != null && obj1.equals(obj2))
            {
                return true;
            }
        }

        return false;
    }
}
