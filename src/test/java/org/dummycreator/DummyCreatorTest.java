/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.opensource.org/licenses/cddl1.php
 * or http://www.opensource.org/licenses/cddl1.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.opensource.org/licenses/cddl1.php.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is dummyCreator. The Initial Developer of the Original
 * Software is Alexander Muthmann <amuthmann@dev-eth0.de>.
 */
package org.dummycreator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dummycreator.TestChainBinding.B;
import org.dummycreator.TestChainBinding.C;
import org.junit.Before;
import org.junit.Test;

public class DummyCreatorTest {

    private DummyCreator dummyCreator;

    @Before
    public void setUp() throws SecurityException, NoSuchMethodException {
	ClassBindings classBindings = ClassBindings.defaultBindings();
	classBindings.add(List.class, ArrayList.class);
	classBindings.add(Integer.class, Integer.class.getConstructor(Integer.TYPE));
	classBindings.add(Long.class, Long.MAX_VALUE);
	classBindings.add(Double.class, Double.MIN_VALUE);
	dummyCreator = new DummyCreator(classBindings);
    }

    @Test
    public void CheckObjectBindings() throws Exception {
	assertEquals(Long.MAX_VALUE, dummyCreator.createDummyOfClass(Long.class), 0);
	assertEquals(Double.MIN_VALUE, dummyCreator.createDummyOfClass(Double.class), 0);
    }

    @Test
    public void CheckMethodBindings() throws Exception {
    }

    @Test
    public void CheckConstructorBindings() throws Exception {
	assertEquals(Integer.class, dummyCreator.createDummyOfClass(Integer.class).getClass());
    }

    @Test
    public void CheckInterfaceBindings() throws Exception {
	assertEquals(ArrayList.class, dummyCreator.createDummyOfClass(List.class).getClass());

	ClassBindings classBindings = new ClassBindings();
	classBindings.add(List.class, ArrayList.class);
	classBindings.add(List.class, LinkedList.class);
	DummyCreator dummyCreator = new DummyCreator(classBindings);
	assertEquals(LinkedList.class, dummyCreator.createDummyOfClass(List.class).getClass());
    }

    @Test
    public void CheckStringCreation() {
	String dummy = dummyCreator.createDummyOfClass(String.class);
	assertEquals(String.class, dummy.getClass());
    }

    @Test
    public void CheckSimpleObjectCreation() {
	assertEquals(Byte.class, dummyCreator.createDummyOfClass(Byte.class).getClass());
	assertEquals(Long.class, dummyCreator.createDummyOfClass(Long.class).getClass());
    }

    @Test
    public void CheckObjectBinding() {
	ClassBindings classBindings = new ClassBindings();
	LinkedList<Object> list = new LinkedList<Object>();
	classBindings.add(List.class, list);
	DummyCreator dummyCreator = new DummyCreator(classBindings);
	List<?> dummy = dummyCreator.createDummyOfClass(List.class);
	assertEquals(LinkedList.class, dummy.getClass());
	assertSame(list, dummy);
    }

    @Test
    public void CheckDeferredSubTypeConstructorBinding() throws SecurityException, NoSuchMethodException {
	ClassBindings classBindings = new ClassBindings();
	classBindings.add(B.class, C.class.getConstructor(int.class));
	DummyCreator dummyCreator = new DummyCreator(classBindings);
	B dummy = dummyCreator.createDummyOfClass(B.class);
	assertEquals(C.class, dummy.getClass());
    }

    @Test
    public void CheckDeferredSubTypeBinding() throws SecurityException, NoSuchMethodException {
	ClassBindings classBindings = new ClassBindings();
	classBindings.add(B.class, C.class);
	DummyCreator dummyCreator = new DummyCreator(classBindings);
	B dummy = dummyCreator.createDummyOfClass(B.class);
	assertEquals(C.class, dummy.getClass());
    }

    @Test
    public void CheckPrimitiveClassCreation() {
	assertEquals(PrimitiveClass.class, dummyCreator.createDummyOfClass(PrimitiveClass.class).getClass());

	assertEquals(InheritedPrimitiveClass.class, dummyCreator.createDummyOfClass(InheritedPrimitiveClass.class).getClass());

	// TODO Check if all parameters have been set
    }

    @Test
    public void CheckNormalClassCreation() {
	assertEquals(NormalClass.class, dummyCreator.createDummyOfClass(NormalClass.class).getClass());
	// TODO Check if all parameters have been set
    }

    @Test
    public void CheckLoopClassCreation() {
	assertEquals(LoopClass.class, dummyCreator.createDummyOfClass(LoopClass.class).getClass());
    }

    @Test
    public void CheckMultiConstructorClassCreation() {
	assertEquals(MultiConstructorClass.class, dummyCreator.createDummyOfClass(MultiConstructorClass.class).getClass());
    }

    @Test
    public void CheckEnumClassCreation() {
	EnumClass ec = dummyCreator.createDummyOfClass(EnumClass.class);
	assertNotNull(ec.getEnumTester());
	assertNotNull(ec.getInternalEnum());
    }

    @SuppressWarnings("serial")
    public static class NumberStringMap extends HashMap<Integer, String> {
    };

    @Test
    public void checkGenericMap() {
	Map<Integer, String> numberStringMap = new NumberStringMap();
	Map<?, ?> ec = dummyCreator.createDummyOfClass(numberStringMap.getClass());
	assertNotNull(ec);
    }

    @SuppressWarnings("serial")
    public static class NumberStringList extends ArrayList<Integer> {
    };

    @Test
    public void checkGenericList() {
	List<Integer> numbers = new NumberStringList();
	List<?> ec = dummyCreator.createDummyOfClass(numbers.getClass());
	assertNotNull(ec);
    }

    @Test
    public void checkList() {
	List<Integer> numbers = new ArrayList<Integer>();
	List<?> ec = dummyCreator.createDummyOfClass(numbers.getClass());
	assertNotNull(ec);
    }

    @Test
    public void checkMooooList() {
	List<MyCustomTestClass> numbers = new MyCustomTestClassList();
	List<?> ec = dummyCreator.createDummyOfClass(numbers.getClass());
	assertNotNull(ec);
    }

    @Test
    public void checkMap() {
	Map<Integer, String> numbers = new HashMap<Integer, String>();
	Map<?, ?> ec = dummyCreator.createDummyOfClass(numbers.getClass());
	assertNotNull(ec);
    }
}