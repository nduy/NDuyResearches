package FOnto;

import java.util.*;

import fuzzyowl2.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.*;


/**
 * Class translating from OWL 2 into fuzzyDL syntax.
 *
 * @author Fernando Bobillo
 */
public class FuzzyOwl2toFuzzyDL extends FuzzyOwl2
{

	private final double DOUBLE_MIN_VALUE = -1000000;
	private final double DOUBLE_MAX_VALUE = 1000000;
	private final double INTEGER_MIN_VALUE = -1000000;
	private final double INTEGER_MAX_VALUE = 1000000;
	
	private Set<String> numericalDatatypes;
	private Set<String> stringDatatypes;


	public FuzzyOwl2toFuzzyDL(String input, String output)
	{
		super(input, output);
		numericalDatatypes = new HashSet<String>(); 
		stringDatatypes = new HashSet<String>();
	}


	@Override
	protected String getIndividualName(OWLIndividual i)
	{
		if (i.isAnonymous())
		{
			printError("Anonymous individual " + i + " not supported");
			return null;
		}
		else
			return( getShortName( i.asOWLNamedIndividual() )) ;
	}


	@Override
	protected String getTopConceptName()
	{
		return("*top*");
	}


	@Override
	protected String getBottomConceptName()
	{
		return("*bottom*");
	}


	@Override
	protected String getAtomicConceptName(OWLClass c)
	{
		return getShortName(c);
	}


	@Override
	protected String getObjectIntersectionOfName(Set<OWLClassExpression> operands)
	{
		String s = "(and ";
		for(OWLClassExpression c : operands)
			s += getClassName(c) + " ";
		s += ")";
		return s;
	}


	@Override
	protected String getObjectUnionOfName(Set<OWLClassExpression> operands)
	{
		String s = "(or ";
		for(OWLClassExpression c : operands)
			s += getClassName(c) + " ";
		s += ")";
		return s;
	}


	@Override
	protected String getObjectSomeValuesFromName(OWLObjectPropertyExpression p, OWLClassExpression c)
	{
		return "(some " + getObjectPropertyName(p) + " " + getClassName(c) + " )";
	}


	@Override
	protected String getObjectAllValuesFromName(OWLObjectPropertyExpression p, OWLClassExpression c)
	{
		return "(all " + getObjectPropertyName(p) + " " + getClassName(c) + " )";
	}


	@Override
	protected String getDataSomeValuesFromName(OWLDataPropertyExpression p, OWLDataRange range)
	{
		DataRangeType type = range.getDataRangeType();
		if (type == DataRangeType.DATATYPE)
		{
			String datatypeName = pm.getShortForm(range.asOWLDatatype());
			if (fuzzyDatatypes.containsKey(datatypeName))
				return "(some " + getDataPropertyName(p) + " " + datatypeName + ")";
		}
		else if (type == DataRangeType.DATA_ONE_OF)
		{
			OWLDataOneOf o = (OWLDataOneOf) range;
			Set<OWLLiteral> set = o.getValues();
			if (set.isEmpty() == false)
			{
				OWLLiteral lit = set.iterator().next();
				return "(= " + getDataPropertyName(p) + " " + lit.getLiteral() + ")";
			}
		}
		printError("Data some values restriction with range " + range + " not supported");
		return null;
	}


	@Override
	protected String getDataAllValuesFromName(OWLDataPropertyExpression p, OWLDataRange range)
	{
		DataRangeType type = range.getDataRangeType();
		if (type == DataRangeType.DATATYPE)
		{
			String datatypeName = pm.getShortForm(range.asOWLDatatype());
			if (fuzzyDatatypes.containsKey(datatypeName))
				return "(all " + getDataPropertyName(p) + " " + datatypeName + ")";
		}
		printError("Data all values restriction with range " + range + " not supported");
		return null;
	}


	@Override
	protected String getObjectComplementOfName(OWLClassExpression c)
	{
		return "(not " + getClassName(c) + " )";
	}


	@Override
	protected String getObjectHasSelfName(OWLObjectPropertyExpression p)
	{
		return "(self " + getObjectPropertyName(p) + ")";
	}


	@Override
	protected String getObjectOneOfName(Set<OWLIndividual> set)
	{
		printError("OneOf concept not supported");
		return null;
	}


	@Override
	protected String getObjectHasValueName(OWLObjectPropertyExpression p, OWLIndividual i)
	{
		printError("Object has value concept not supported");
		return null;
	}


	@Override
	protected String getDataHasValueName(OWLDataPropertyExpression p, OWLLiteral lit)
	{
		printError("Data has value concept not supported");
		return null;
	}


	@Override
	protected String getObjectMinCardinalityRestrictionName(int card, OWLObjectPropertyExpression p, OWLClassExpression c)
	{
		printError("Object min cardinality restriction not supported");
		return null;
	}


	@Override
	protected String getObjectMinCardinalityRestrictionName(int card, OWLObjectPropertyExpression p)
	{
		printError("Object min cardinality restriction not supported");
		return null;
	}


	@Override
	protected String getObjectMaxCardinalityRestrictionName(int card, OWLObjectPropertyExpression p, OWLClassExpression c)
	{
		printError("Object max cardinality restriction not supported");
		return null;
	}


	@Override
	protected String getObjectMaxCardinalityRestrictionName(int card, OWLObjectPropertyExpression p)
	{
		printError("Object max cardinality restriction not supported");
		return null;
	}


	@Override
	protected String getObjectExactCardinalityRestrictionName(int card, OWLObjectPropertyExpression p, OWLClassExpression c)
	{
		printError("Object exact cardinality restriction not supported");
		return null;
	}


	@Override
	protected String getObjectExactCardinalityRestrictionName(int card, OWLObjectPropertyExpression p)
	{
		printError("Object exact cardinality restriction not supported");
		return null;
	}


	@Override
	protected String getDataMinCardinalityRestrictionName(int card, OWLDataPropertyExpression p, OWLDataRange range)
	{
		printError("Data min cardinality restriction not supported");
		return null;
	}


	@Override
	protected String getDataMinCardinalityRestrictionName(int card, OWLDataPropertyExpression p)
	{
		printError("Data min cardinality restriction not supported");
		return null;
	}


	@Override
	protected String getDataMaxCardinalityRestrictionName(int card, OWLDataPropertyExpression p, OWLDataRange range)
	{
		printError("Data max cardinality restriction not supported");
		return null;
	}


	@Override
	protected String getDataMaxCardinalityRestrictionName(int card, OWLDataPropertyExpression p)
	{
		printError("Data max cardinality restriction not supported");
		return null;
	}


	@Override
	protected String getDataExactCardinalityRestrictionName(int card, OWLDataPropertyExpression p, OWLDataRange range)
	{
		printError("Data exact cardinality restriction not supported");
		return null;
	}


	@Override
	protected String getDataExactCardinalityRestrictionName(int card, OWLDataPropertyExpression p)
	{
		printError("Data exact cardinality restriction not supported");
		return null;
	}


	@Override
	protected String getTopObjectPropertyName()
	{
		printError("Top object property not supported");
		return null;
	}


	@Override
	protected String getBottomObjectPropertyName()
	{
		printError("Bottom object property not supported");
		return null;
	}


	@Override
	protected String getAtomicObjectPropertyName(OWLObjectProperty p)
	{
		return getShortName(p);
	}


	@Override
	protected String getTopDataPropertyName()
	{
		printError("Top data property not supported");
		return null;
	}


	@Override
	protected String getBottomDataPropertyName()
	{
		printError("Bottom data property not supported");
		return null;
	}


	@Override
	protected String getAtomicDataPropertyName(OWLDataProperty p)
	{
		return getShortName(p);
	}


	@Override
	protected void writeFuzzyLogic(FuzzyLogic logic)
	{
		print("(define-fuzzy-logic " + logic + ")");
	}


	@Override
	protected void writeConceptDeclaration(OWLClassExpression c)
	{
		print("(define-primitive-concept " + getClassName(c) + " " + getTopConceptName() + " )");
	}


	@Override
	protected void writeDataPropertyDeclaration(OWLDataPropertyExpression dp)
	{
		writeFunctionalDataPropertyAxiom(dp);
		print("(range " + getDataPropertyName(dp) + " *string* )");
	}


	@Override
	protected void writeObjectPropertyDeclaration(OWLObjectPropertyExpression op)
	{

	}


	@Override
	protected void writeTriangularModifierDefinition(String name, TriangularModifier mod)
	{
		double a = mod.getA();
		double b = mod.getB();
		double c = mod.getC();
		print("(define-modifier " + name + " triangular-modifier(" + a + ", " + b + ", " + c + ") )");
	}


	@Override
	protected void writeLinearModifierDefinition(String name, LinearModifier mod)
	{
		double c = mod.getC();
		print("(define-modifier " + name + " linear-modifier(" + c + ") )");
	}


	@Override
	protected void writeLeftShoulderFunctionDefinition(String name, LeftShoulderFunction f)
	{
		double k1 = f.getMinValue();
		double k2 = f.getMaxValue();
		double a = f.getA();
		double b = f.getB();
		print("(define-fuzzy-concept " + name + " left-shoulder(" +
			k1 + ", " + k2 + ", " + a + ", " + b + ") )");
	}


	@Override
	protected void writeRightShoulderFunctionDefinition(String name, RightShoulderFunction f)
	{
		double k1 = f.getMinValue();
		double k2 = f.getMaxValue();
		double a = f.getA();
		double b = f.getB();
		print("(define-fuzzy-concept " + name + " right-shoulder(" +
			k1 + ", " + k2 + ", " + a + ", " + b + ") )");
	}


	@Override
	protected void writeLinearFunctionDefinition(String name, LinearFunction f)
	{
		double k1 = f.getMinValue();
		double k2 = f.getMaxValue();
		double a = f.getA();
		double b = f.getB();
		print("(define-fuzzy-concept " + name + " linear(" +
			k1 + ", " + k2 + ", " + a + ", " + b + ") )");
	}


	@Override
	protected void writeTriangularFunctionDefinition(String name, TriangularFunction f)
	{
		double k1 = f.getMinValue();
		double k2 = f.getMaxValue();
		double a = f.getA();
		double b = f.getB();
		double c = f.getC();
		print("(define-fuzzy-concept " + name + " triangular(" +
			k1 + ", " + k2 + ", " + a + ", " + b + ", " + c + ") )");
	}


	@Override
	protected void writeTrapezoidalFunctionDefinition(String name, TrapezoidalFunction f)
	{
		double k1 = f.getMinValue();
		double k2 = f.getMaxValue();
		double a = f.getA();
		double b = f.getB();
		double c = f.getC();
		double d = f.getD();
		print("(define-fuzzy-concept " + name + " trapezoidal(" +
			k1 + ", " + k2 + ", " + a + ", " + b + ", " + c + ", " + d + ") )");
	}


	@Override
	protected void writeModifiedFunctionDefinition(String name, ModifiedFunction dat)
	{
		print("(define-concept " + name + " ( " + dat.getMod() + " " + dat.getD() + " ) )");
	}


	@Override
	protected void writeModifiedPropertyDefinition(String name, ModifiedProperty c)
	{
		printError("Modified property not supported");
	}


	@Override
	protected void writeModifiedConceptDefinition(String name, ModifiedConcept c)
	{
		print("(define-concept " + name + " (" + c.getFuzzyModifier() + " " +
			c.getFuzzyConcept() + ") )");
	}


	@Override
	protected void writeFuzzyNominalConceptDefinition(String name, FuzzyNominalConcept c)
	{
		printError("Fuzzy nominal not supported");
	}


	@Override
	protected void writeWeightedConceptDefinition(String name, WeightedConcept c)
	{
		print("(define-concept " + name + " (" + c.getWeight() + " " +
			c.getFuzzyConcept() + ") )");
	}


	@Override
	protected void writeWeightedMaxConceptDefinition(String name, WeightedMaxConcept c)
	{
		String s = "(define-concept " + name + " (w-max ";
		List<WeightedConcept> list = c.getWeightedConcepts();
		WeightedConcept wc = list.get(0);
		s += "(" + wc.getWeight() + " " + wc.getFuzzyConcept().toString() + ")";

		for(int i=1; i<list.size(); i++)
		{
			wc = list.get(i);
			s += " (" + wc.getWeight() + " " + wc.getFuzzyConcept() + ")";
		}
		s += " ) )";
		print(s);
	}


	@Override
	protected void writeWeightedMinConceptDefinition(String name, WeightedMinConcept c)
	{
		String s = "(define-concept " + name + " (w-min ";
		List<WeightedConcept> list = c.getWeightedConcepts();
		WeightedConcept wc = list.get(0);
		s += "(" + wc.getWeight() + " " + wc.getFuzzyConcept().toString() + ")";

		for(int i=1; i<list.size(); i++)
		{
			wc = list.get(i);
			s += " (" + wc.getWeight() + " " + wc.getFuzzyConcept() + ")";
		}
		s += " ) )";
		print(s);
	}


	@Override
	protected void writeWeightedSumConceptDefinition(String name, WeightedSumConcept c)
	{
		String s = "(define-concept " + name + " (w-sum ";
		List<WeightedConcept> list = c.getWeightedConcepts();
		WeightedConcept wc = list.get(0);
		s += "(" + wc.getWeight() + " " + wc.getFuzzyConcept().toString() + ")";

		for(int i=1; i<list.size(); i++)
		{
			wc = list.get(i);
			s += " (" + wc.getWeight() + " " + wc.getFuzzyConcept() + ")";
		}
		s += " ) )";
		print(s);
	}


	@Override
	protected void writeOwaConceptDefinition(String name, OwaConcept c)
	{
		String s = "(define-concept " + name + " (owa (";
		List<Double> w = c.getWeights();
		s += w.get(0);
		for(int i=1; i<w.size(); i++)
			s += " " + w.get(i).toString();
		s += ") (";
		List<String> list = c.getConcepts();
		s += list.get(0);
		for(int i=1; i<list.size(); i++)
			s += " " + list.get(i).toString();
		s += ") ) )";
		print(s);
	}


	@Override
	protected void writeChoquetConceptDefinition(String name, ChoquetConcept c)
	{
		String s = "(define-concept " + name + " (choquet (";
		List<Double> w = c.getWeights();
		s += w.get(0);
		for(int i=1; i<w.size(); i++)
			s += " " + w.get(i).toString();
		s += ") (";
		List<String> list = c.getConcepts();
		s += list.get(0);
		for(int i=1; i<list.size(); i++)
			s += " " + list.get(i).toString();
		s += ") ) )";
		print(s);
	}


	@Override
	protected void writeSugenoConceptDefinition(String name, SugenoConcept c)
	{
		String s = "(define-concept " + name + " (sugeno (";
		List<Double> w = c.getWeights();
		s += w.get(0);
		for(int i=1; i<w.size(); i++)
			s += " " + w.get(i).toString();
		s += ") (";
		List<String> list = c.getConcepts();
		s += list.get(0);
		for(int i=1; i<list.size(); i++)
			s += " " + list.get(i).toString();
		s += ") ) )";
		print(s);
	}


	@Override
	protected void writeQuasiSugenoConceptDefinition(String name, QuasiSugenoConcept c)
	{
		String s = "(define-concept " + name + " (q-sugeno (";
		List<Double> w = c.getWeights();
		s += w.get(0);
		for(int i=1; i<w.size(); i++)
			s += " " + w.get(i).toString();
		s += ") (";
		List<String> list = c.getConcepts();
		s += list.get(0);
		for(int i=1; i<list.size(); i++)
			s += " " + list.get(i).toString();
		s += ") ) )";
		print(s);
	}


	@Override
	protected void writeQowaConceptDefinition(String name, QowaConcept c)
	{
		String s = "(define-concept " + name + " (q-owa " + c.getQuantifier() + " ";
		List<String> list = c.getConcepts();
		s += list.get(0);
		for(int i=1; i<list.size(); i++)
			s += " " + list.get(i).toString();
		s += ") )";
		print(s);
	}


	@Override
	protected void writeConceptAssertionAxiom(OWLIndividual i, OWLClassExpression c, double d)
	{
		print("(instance " + getIndividualName(i) + " " + getClassName(c) + " " + d + ")");
	}


	@Override
	protected void writeObjectPropertyAssertionAxiom(OWLIndividual i1, OWLIndividual i2, OWLObjectPropertyExpression p, double d)
	{
		print("(related " + getIndividualName(i1) + " " + getIndividualName(i2) + " " + getObjectPropertyName(p) + " " + d + ")");
	}


	@Override
	protected void writeDataPropertyAssertionAxiom(OWLIndividual i1, OWLLiteral i2, OWLDataPropertyExpression p, double d)
	{
		OWLDatatype dat = i2.getDatatype();
		String dpName = getDataPropertyName(p);

		if (dat == null)
			print("(instance " + getIndividualName(i1) + " (= " + dpName + " " + i2.getLiteral() + ") " + d + " )");
		else
		{
			String datatypeName = pm.getShortForm(dat.asOWLDatatype());
			if (fuzzyDatatypes.containsKey(datatypeName))				
				print("(instance " + getIndividualName(i1) + " (some " + dpName + " " + datatypeName + ") " + d + " )");
			else
			{
				String l = i2.getLiteral();
				//String l2 = l.replaceAll("\\.", ",");
				//Scanner scanner = new Scanner(l2);
				//if (scanner.hasNextDouble() || scanner.hasNextInt())
				if (i2.isDouble() || i2.isInteger() || i2.isFloat())
				{
					if (! numericalDatatypes.contains(dpName))
					{
						numericalDatatypes.add(dpName);
						writeFunctionalDataPropertyAxiom(p);
						if (i2.isInteger())	
							print("(range " + getDataPropertyName(p) + " *integer* " + INTEGER_MIN_VALUE + " " + INTEGER_MAX_VALUE + " )");
						else
							print("(range " + getDataPropertyName(p) + " *real* " + DOUBLE_MIN_VALUE + " " + DOUBLE_MAX_VALUE + " )");
							
					}
					
					if (i2.isDouble())
						print("(instance " + getIndividualName(i1) + " (= " + dpName + " " + i2.parseDouble() + ") " + d + " )");					
					else if (i2.isInteger())
						print("(instance " + getIndividualName(i1) + " (= " + dpName + " " + i2.parseInteger() + ") " + d + " )");
					else 
						print("(instance " + getIndividualName(i1) + " (= " + dpName + " " + i2.parseFloat() + ") " + d + " )");
				}

				else
				{
					if (! stringDatatypes.contains(dpName))
					{
						stringDatatypes.add(dpName);
						writeDataPropertyDeclaration(p);
					}
					// Convert separators into "_"
					l = l.replaceAll("\\s", "_");
					// If first character is a number, add a "_"
					char c = l.charAt(0);
					if ((c >= '0') && (c <= '9'))
						l = "_" + l;
					print("(instance " + getIndividualName(i1) + " (= " + dpName + " \"" + l + "\" ) " + d + " )");
				}
			}
		}
	}


	@Override
	protected void writeNegativeObjectPropertyAssertionAxiom(OWLIndividual i1, OWLIndividual i2, OWLObjectPropertyExpression p, double d)
	{
		printError("Negative object property assertion not supported");
	}


	@Override
	protected void writeNegativeDataPropertyAssertionAxiom(OWLIndividual i1, OWLLiteral i2, OWLDataPropertyExpression p, double d)
	{
		printError("Negative data property assertion not supported");
	}


	@Override
	protected void writeSameIndividualAxiom(Set<OWLIndividual> set)
	{
		printError("Same individual axiom not supported");
	}


	@Override
	protected void writeDifferentIndividualsAxiom(Set<OWLIndividual> set)
	{
		printError("Different individuals axiom not supported");
	}


	@Override
	protected void writeDisjointClassesAxiom(Set<OWLClassExpression> set)
	{
		if (set.size() > 1)
		{
			String s = "(disjoint ";
			for (OWLClassExpression c : set)
/*			{
/*				ClassExpressionType type = c.getClassExpressionType();
				if (type != ClassExpressionType.OWL_CLASS)
				{
					System.out.println("Concept type " + type + " not supported in disjoint classes axiom");
					return;
				}
				else
*/					s += getShortName(c.asOWLClass()) + " ";
//			}
			s += ")";
			print(s);
		}
	}


	@Override
	protected void writeDisjointUnionAxiom(Set<OWLClassExpression> set)
	{
		if (set.size() > 1)
		{
			String s = "(disjoint-union ";
			for (OWLClassExpression c : set)
			{
				ClassExpressionType type = c.getClassExpressionType();
				if (type != ClassExpressionType.OWL_CLASS)
					exit("Concept type " + type + " not supported in disjoint union axiom");
				else
					s += getShortName(c.asOWLClass()) + " ";
			}
			s += ")";
			print(s);
		}
	}


	@Override
	protected void writeSubclassOfAxiom(OWLClassExpression subclass, OWLClassExpression superclass, double d)
	{
//		if (superclass.isOWLThing() != false)
		{
			ClassExpressionType type = subclass.getClassExpressionType();
			if ((type == ClassExpressionType.OWL_CLASS) && (d == 1))
				print("(define-primitive-concept " + getShortName(subclass.asOWLClass()) + " " + getClassName(superclass) + ")");
			else
				print("(implies " + getClassName(subclass) + " " + getClassName(superclass) + " " + d + ")");
		}
	}


	@Override
	protected void writeEquivalentClassesAxiom(Set<OWLClassExpression> set)
	{
		String name = null;
		OWLClassExpression leftClass = null;
		for (OWLClassExpression c : set)
			if (c.getClassExpressionType() == ClassExpressionType.OWL_CLASS)
			{
				name = getShortName(c.asOWLClass());
				leftClass = c;
				break;
			}

		if (name == null)
			exit("Equivalent classes axiom " + set + " require at least one atomic class");

		for (OWLClassExpression c : set)
			if (c != leftClass)
				print("(define-concept " + name + " " + getClassName(c) + " )");
	}


	@Override
	protected void writeSubObjectPropertyOfAxiom(OWLObjectPropertyExpression subProperty, OWLObjectPropertyExpression superProperty, double d)
	{
//		if (superProperty.isOWLTopObjectProperty() == false)
			print("(implies-role " + getObjectPropertyName(subProperty) + " " + getObjectPropertyName(superProperty) + " " + d + ")");
	}


	@Override
	protected void writeSubPropertyChainOfAxiom(List<OWLObjectPropertyExpression> chain, OWLObjectPropertyExpression superProperty, double d)
	{
		printError("Subproperty chain axiom not supported");
	}


	@Override
	protected void writeSubDataPropertyOfAxiom(OWLDataPropertyExpression subProperty, OWLDataPropertyExpression superProperty, double d)
	{
//		if (superProperty.isOWLTopDataProperty() == false)
			print("(implies-role " + getDataPropertyName(subProperty) + " " + getDataPropertyName(superProperty) + " " + d + ")");
	}


	@Override
	protected void writeEquivalentObjectPropertiesAxiom(Set<OWLObjectPropertyExpression> set)
	{
		Iterator<OWLObjectPropertyExpression> it = set.iterator();
		OWLObjectPropertyExpression first = it.next();
		while(it.hasNext())
		{
			OWLObjectPropertyExpression property = it.next();
			print("(implies-role " + getObjectPropertyName(first) + " " + getObjectPropertyName(property) + ")");
			print("(implies-role " + getObjectPropertyName(property) + " " + getObjectPropertyName(first) + ")");
		}
	}


	@Override
	protected void writeEquivalentDataPropertiesAxiom(Set<OWLDataPropertyExpression> set)
	{
		Iterator<OWLDataPropertyExpression> it = set.iterator();
		OWLDataPropertyExpression first = it.next();
		while(it.hasNext())
		{
			OWLDataPropertyExpression property = it.next();
			print("(implies-role " + getDataPropertyName(first) + " " + getDataPropertyName(property) + ")");
			print("(implies-role " + getDataPropertyName(property) + " " + getDataPropertyName(first) + ")");
		}
	}


	@Override
	protected void writeTransitiveObjectPropertyAxiom(OWLObjectPropertyExpression p)
	{
		print("(transitive " + getObjectPropertyName(p) + ")");
	}


	@Override
	protected void writeSymmetricObjectPropertyAxiom(OWLObjectPropertyExpression p)
	{
		print("(symmetric " + getObjectPropertyName(p) + ")");
	}


	@Override
	protected void writeAsymmetricObjectPropertyAxiom(OWLObjectPropertyExpression p)
	{
		printError("Asymmetric object property axiom not supported");
	}


	@Override
	protected void writeReflexiveObjectPropertyAxiom(OWLObjectPropertyExpression p)
	{
		print("(reflexive " + getObjectPropertyName(p) + ")");
	}


	@Override
	protected void writeIrreflexiveObjectPropertyAxiom(OWLObjectPropertyExpression p)
	{
		printError("Irreflexive object property axiom not supported");
	}


	@Override
	protected void writeFunctionalObjectPropertyAxiom(OWLObjectPropertyExpression p)
	{
		print("(functional " + getObjectPropertyName(p) + ")");
	}


	@Override
	protected void writeFunctionalDataPropertyAxiom(OWLDataPropertyExpression p)
	{
		print("(functional " + getDataPropertyName(p) + ")");
	}


	@Override
	protected void writeInverseObjectPropertiesAxiom(OWLObjectPropertyExpression p1, OWLObjectPropertyExpression p2)
	{
		print("(inverse " + getObjectPropertyName(p1) + " " + getObjectPropertyName(p2) + ")");
	}


	@Override
	protected void writeInverseFunctionalObjectPropertyAxiom(OWLObjectPropertyExpression p)
	{
		print("(inverse-functional " + getObjectPropertyName(p) + ")");
	}


	@Override
	protected void writeObjectPropertyDomainAxiom(OWLObjectPropertyExpression p, OWLClassExpression c)
	{
		print("(domain " + getObjectPropertyName(p) + " " + getClassName(c) + " )");
	}


	@Override
	protected void writeObjectPropertyRangeAxiom(OWLObjectPropertyExpression p, OWLClassExpression c)
	{
		print("(range " + getObjectPropertyName(p) + " " + getClassName(c) + " )");
	}


	@Override
	protected void writeDataPropertyDomainAxiom(OWLDataPropertyExpression p, OWLClassExpression c)
	{
		print("(domain " + getDataPropertyName(p) + " " + getClassName(c) + " )");
	}


	@Override
	protected void writeDataPropertyRangeAxiom(OWLDataPropertyExpression p, OWLDataRange range)
	{
		String rangeString = null;
		String dpName = getDataPropertyName(p);
		DataRangeType type = range.getDataRangeType();

		if (type == DataRangeType.DATATYPE)
		{
			OWLDatatype datatype = range.asOWLDatatype();
			if (datatype.isString())
			{
				stringDatatypes.add(getDataPropertyName(p));
				rangeString = "*string*";
			}
		}
		else if (type == DataRangeType.DATA_INTERSECTION_OF)
		{
			int correctness = 0;
			int isInteger = 0;
			double min = 0;
			double max = 0;

			Set<OWLDataRange> set = ((OWLDataIntersectionOf) range).getOperands();
			if (set.size() == 2)
			{
				for (OWLDataRange dr2 : set)
				{
					if (dr2.getDataRangeType() == DataRangeType.DATATYPE_RESTRICTION)
					{
						Set<OWLFacetRestriction> set2 = ((OWLDatatypeRestriction) dr2).getFacetRestrictions();
						if (set2.size() != 1)
							continue;

						OWLFacetRestriction facet = set2.iterator().next();
						String val = facet.getFacetValue().getLiteral();
						if (facet.getFacetValue().isInteger())
							isInteger++;
						double k = Double.parseDouble(val);
						if (facet.getFacet() == OWLFacet.MIN_INCLUSIVE)
						{
							min = k;
							correctness++;
						}
						else if (facet.getFacet() == OWLFacet.MAX_INCLUSIVE)
						{
							max = k;
							correctness++;
						}
					}
				}
			}

			if (correctness == 2)
			{
				if (isInteger == 2)
					rangeString = "*integer* " + ((int) min) + " " + ((int) max) ;
				else
					rangeString = "*real* " + min + " " + max;
				numericalDatatypes.add(dpName);
			}
		}
		if (rangeString != null)
		{
			writeFunctionalDataPropertyAxiom(p);
			print("(range " + dpName + " " + rangeString + " )");
		}
		else
		{
			if (type.toString().equals("DATA_ONE_OF"))
				printError("Data one of range axiom not supported");
			else
			{
				OWLDatatype rangeType = range.asOWLDatatype();
				if (rangeType.isFloat() || rangeType.isDouble() )
				{
					writeFunctionalDataPropertyAxiom(p);
					print("(range " + dpName + " *real* " + DOUBLE_MIN_VALUE + " " + DOUBLE_MAX_VALUE + " )");
					numericalDatatypes.add(dpName);
				}
				else if (rangeType.isInteger())
				{
					writeFunctionalDataPropertyAxiom(p);
					print("(range " + dpName + " *integer* " + INTEGER_MIN_VALUE + " " + INTEGER_MAX_VALUE + " )");
					numericalDatatypes.add(dpName);
				}
				else
					printError("Data property range axiom with range " + range + " not supported");
			}
		}
	}


	@Override
	protected void writeDisjointObjectPropertiesAxiom(Set<OWLObjectPropertyExpression> set)
	{
		printError("Disjoint object properties axiom not supported");
	}


	@Override
	protected void writeDisjointDataPropertiesAxiom(Set<OWLDataPropertyExpression> set)
	{
		printError("Disjoint data properties axiom not supported");
	}


	/**
	 * @param args Two arguments: the input OWL 2 ontology, and the output fuzzy ontology in fuzzyDL syntax.
	 */
	public static void startTranslation(String[] args, InputDataBlock[] argIn, Path tempfile)
	{
		String[] returnValue = processParameters(args);
                System.out.println("URL1=============== ARG Proccess done!");
                System.out.println("URL1.1===============: "+ returnValue[0]);
                System.out.println("URL1.2===============: "+ returnValue[1]);
//		FuzzyOwl2toFuzzyDL f = new FuzzyOwl2toFuzzyDL(returnValue[0], returnValue[1]);
                FuzzyOwl2toFuzzyDL f = new FuzzyOwl2toFuzzyDL(args[0],args[1]);
                System.out.println("URL2=============== ARG Proccess done!");
		f.translateOwl2Ontology();
        /// ADD QUERIES CODE HERE
                String qStr = MakingQueriesString(argIn[0], argIn[1], argIn[2], argIn[3],argIn[4]);
        /// Now write into temp file
                Charset charset = Charset.forName("UTF-8");
              
                try (BufferedWriter writer = Files.newBufferedWriter(tempfile , charset)) {
                    writer.write(qStr, 0, qStr.length());
                } catch (IOException x) {
                    System.err.format("IOException: %s%n", x);
                }
	}
	
	private boolean isReservedWord(String s)
	{
		if (s.equals("linear"))
			return true;
		if (s.equals("triangular"))
			return true;
		if (s.equals("trapezoidal"))
			return true;
		if (s.equals("crisp"))
			return true;
		if (s.equals("classical"))
			return true;
		if (s.equals("disjoint"))
			return true;
		if (s.equals("instance"))
			return true;
		if (s.equals("related"))
			return true;
		if (s.equals("domain"))
			return true;
		if (s.equals("range"))
			return true;
		
		// Avoid numbers
	    try { 
	        Double.parseDouble(s);
	        return true;
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	}


	@Override
	protected String getShortName(OWLEntity e)
	{
		String aux = pm.getShortForm(e);
		if (isReservedWord(aux))
			return "_" + aux;
		else
			return aux;
	}
        
        public static String MakingQueriesString(InputDataBlock bkl0,InputDataBlock bkl1, InputDataBlock bkl2, InputDataBlock bkl3, InputDataBlock bkl4){
        String str="";
         //////// Make Query Strings
        str = str
                + ProcessInput(bkl0) + "\n"
                + ProcessInput(bkl1) + "\n"
                + ProcessInput(bkl2) + "\n"
                + ProcessInput(bkl3) + "\n"
                + ProcessInput(bkl4) + "\n";
        return str;
    }
    
    public static String ProcessInput(InputDataBlock inBlock)
    {
        String str="";
        if (inBlock.jCheck.isSelected()) {
            switch (inBlock.jCom.getSelectedIndex()){
                case 0:
                    str="(max-instance? "+ inBlock.jText1.getText()+" "+inBlock.jText2.getText() +")";
                    break;
                case 1:
                    str="(min-instance? "+inBlock.jText1.getText() +" "+inBlock.jText2.getText() +")";
                    break;
                case 2:
                    str="(all-instances? "+inBlock.jText1.getText() +")";
                    break;
                case 3:
                    str="(max-related? "+inBlock.jText1.getText() +" "+inBlock.jText2.getText() +" "+inBlock.jText3.getText() +")";
                    break;
                case 4:
                    str="(min-related? "+inBlock.jText1.getText() +" "+inBlock.jText2.getText() +" "+inBlock.jText3.getText() +")";
                    break;
                case 5:
                    str="(max-subs? "+inBlock.jText1.getText() +" "+inBlock.jText2.getText() +")";
                    break;
                case 6:
                    str="(min-subs? "+inBlock.jText1.getText() +" "+inBlock.jText2.getText() +")";
                    break;
                case 7:
                    str="(g-max-subs? "+inBlock.jText1.getText() +" "+inBlock.jText2.getText() +")";
                    break;
                case 8:
                    str="(g-min-subs? "+inBlock.jText1.getText() +" "+inBlock.jText2.getText() +")";
                    break;
                case 9:
                    str="(l-max-subs? "+inBlock.jText1.getText() +" "+inBlock.jText2.getText() +")";
                    break;
                case 10:
                    str="(l-min-subs? "+inBlock.jText1.getText() +" "+inBlock.jText2.getText() +")";
                    break;
                case 11:
                    str="(kd-max-subs? "+inBlock.jText1.getText() +" "+inBlock.jText2.getText() +")";
                    break;
                case 12:
                    str="(kd-min-subs? "+inBlock.jText1.getText() +" "+inBlock.jText2.getText() +")";
                    break;
            }
         }
        return str;
        }
    public static class InputDataBlock {
            
        public JCheckBox jCheck;
        public JComboBox jCom;
        public JTextField jText1;
        public JTextField jText2;
        public JTextField jText3;
        InputDataBlock (JCheckBox jCheck_, JComboBox jCom_,JTextField jText1_,JTextField jText2_,JTextField jText3_){
            jCheck=jCheck_; 
            jCom=jCom_;
            jText1=jText1_;
            jText2=jText2_;
            jText3=jText3_;
        }
        InputDataBlock(InputDataBlock blk){
            jCheck=blk.jCheck; 
            jCom=blk.jCom;
            jText1=blk.jText1;
            jText2=blk.jText2;
            jText3=blk.jText3;           
        }


    }
}
