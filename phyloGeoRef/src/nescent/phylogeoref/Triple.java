/*
 *  Copyright (C) 2010 Kathryn Iverson <kd.iverson at gmail.com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package nescent.phyloGeoRef;

/**
 *
 * @param <A> 
 * @param <B> 
 * @param <C>
 * @author Kathryn Iverson <kd.iverson at gmail.com>
 */
public class Triple<A, B, C> {
    private A first;
    private B second;
    private C third;

    public Triple(A first, B second, C third) {
        super();
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Triple) {
                Triple otherTriple = (Triple) other;
                return
                ((  this.first == otherTriple.first || ( this.first != null && otherTriple.first != null && this.first.equals(otherTriple.first))) &&
                 (  this.second == otherTriple.second || ( this.second != null && otherTriple.second != null && this.second.equals(otherTriple.second))) &&
                 (  this.third == otherTriple.third || (this.third != null && otherTriple.third != null && this.third.equals(otherTriple.third)))
                 );
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.first != null ? this.first.hashCode() : 0);
        hash = 11 * hash + (this.second != null ? this.second.hashCode() : 0);
        hash = 11 * hash + (this.third != null ? this.third.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString()
    {
           return "(" + first + ", " + second + ", " + third + ")";
    }

    public A getFirst() {
        return first;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    public B getSecond() {
        return second;
    }

    public void setSecond(B second) {
        this.second = second;
    }

    public C getThird() {
        return third;
    }

    public void setThird (C third) {
        this.third = third;
    }


}
