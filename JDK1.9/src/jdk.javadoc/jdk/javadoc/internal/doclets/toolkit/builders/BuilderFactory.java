/*
 * Copyright (c) 2003, 2016, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package jdk.javadoc.internal.doclets.toolkit.builders;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.ModuleElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import jdk.javadoc.internal.doclets.toolkit.AnnotationTypeWriter;
import jdk.javadoc.internal.doclets.toolkit.ClassWriter;
import jdk.javadoc.internal.doclets.toolkit.Configuration;
import jdk.javadoc.internal.doclets.toolkit.PropertyWriter;
import jdk.javadoc.internal.doclets.toolkit.WriterFactory;
import jdk.javadoc.internal.doclets.toolkit.util.ClassTree;

/**
 * The factory for constructing builders.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @author Jamie Ho
 */

public class BuilderFactory {

    /**
     * The current configuration of the doclet.
     */
    private final Configuration configuration;

    /**
     * The factory to retrieve the required writers from.
     */
    private final WriterFactory writerFactory;

    private final AbstractBuilder.Context context;

    /**
     * Construct a builder factory using the given configuration.
     * @param configuration the configuration for the current doclet
     * being executed.
     */
    public BuilderFactory (Configuration configuration) {
        this.configuration = configuration;
        this.writerFactory = configuration.getWriterFactory();

        Set<PackageElement> containingPackagesSeen = new HashSet<>();
        context = new AbstractBuilder.Context(configuration, containingPackagesSeen,
                LayoutParser.getInstance(configuration));
    }

    /**
     * Return the builder that builds the constant summary.
     * @return the builder that builds the constant summary.
     */
    public AbstractBuilder getConstantsSummaryBuilder() {
        return ConstantsSummaryBuilder.getInstance(context,
            writerFactory.getConstantsSummaryWriter());
    }

    /**
     * Return the builder that builds the package summary.
     *
     * @param pkg the package being documented.
     * @param prevPkg the previous package being documented.
     * @param nextPkg the next package being documented.
     * @return the builder that builds the constant summary.
     */
    public AbstractBuilder getPackageSummaryBuilder(PackageElement pkg, PackageElement prevPkg,
            PackageElement nextPkg) {
        return PackageSummaryBuilder.getInstance(context, pkg,
            writerFactory.getPackageSummaryWriter(pkg, prevPkg, nextPkg));
    }

    /**
     * Return the builder that builds the module summary.
     *
     * @param mdle the module being documented.
     * @param prevModule the previous module being documented.
     * @param nextModule the next module being documented.
     * @return the builder that builds the module summary.
     */
    public AbstractBuilder getModuleSummaryBuilder(ModuleElement mdle, ModuleElement prevModule,
            ModuleElement nextModule) {
        return ModuleSummaryBuilder.getInstance(context, mdle,
            writerFactory.getModuleSummaryWriter(mdle, prevModule, nextModule));
    }

    /**
     * Return the builder for the class.
     *
     * @param typeElement the class being documented.
     * @param prevClass the previous class that was documented.
     * @param nextClass the next class being documented.
     * @param classTree the class tree.
     * @return the writer for the class.  Return null if this
     * writer is not supported by the doclet.
     */
    public AbstractBuilder getClassBuilder(TypeElement typeElement,
            TypeElement prevClass, TypeElement nextClass, ClassTree classTree) {
        return ClassBuilder.getInstance(context, typeElement,
            writerFactory.getClassWriter(typeElement, prevClass, nextClass, classTree));
    }

    /**
     * Return the builder for the annotation type.
     *
     * @param annotationType the annotation type being documented.
     * @param prevType the previous type that was documented.
     * @param nextType the next type being documented.
     * @return the writer for the annotation type.  Return null if this
     * writer is not supported by the doclet.
     */
    public AbstractBuilder getAnnotationTypeBuilder(
        TypeElement annotationType, TypeMirror prevType, TypeMirror nextType) {
        return AnnotationTypeBuilder.getInstance(context, annotationType,
            writerFactory.getAnnotationTypeWriter(annotationType, prevType, nextType));
    }

    /**
     * Return an instance of the method builder for the given class.
     *
     * @return an instance of the method builder for the given class.
     */
    public AbstractBuilder getMethodBuilder(ClassWriter classWriter) {
        return MethodBuilder.getInstance(context, classWriter.getTypeElement(),
            writerFactory.getMethodWriter(classWriter));
    }

    /**
     * Return an instance of the annotation type fields builder for the given
     * class.
     *
     * @return an instance of the annotation type field builder for the given
     *         annotation type.
     */
    public AbstractBuilder getAnnotationTypeFieldsBuilder(
            AnnotationTypeWriter annotationTypeWriter) {
        return AnnotationTypeFieldBuilder.getInstance(context,
                annotationTypeWriter.getAnnotationTypeElement(),
                writerFactory.getAnnotationTypeFieldWriter(annotationTypeWriter));
    }

    /**
     * Return an instance of the annotation type member builder for the given
     * class.
     *
     * @return an instance of the annotation type member builder for the given
     *         annotation type.
     */
    public AbstractBuilder getAnnotationTypeOptionalMemberBuilder(
            AnnotationTypeWriter annotationTypeWriter) {
        return AnnotationTypeOptionalMemberBuilder.getInstance(context,
            annotationTypeWriter.getAnnotationTypeElement(),
            writerFactory.getAnnotationTypeOptionalMemberWriter(annotationTypeWriter));
    }

    /**
     * Return an instance of the annotation type member builder for the given
     * class.
     *
     * @return an instance of the annotation type member builder for the given
     *         annotation type.
     */
    public AbstractBuilder getAnnotationTypeRequiredMemberBuilder(
            AnnotationTypeWriter annotationTypeWriter) {
        return AnnotationTypeRequiredMemberBuilder.getInstance(context,
            annotationTypeWriter.getAnnotationTypeElement(),
            writerFactory.getAnnotationTypeRequiredMemberWriter(annotationTypeWriter));
    }

    /**
     * Return an instance of the enum constants builder for the given class.
     *
     * @return an instance of the enum constants builder for the given class.
     */
    public AbstractBuilder getEnumConstantsBuilder(ClassWriter classWriter) {
        return EnumConstantBuilder.getInstance(context, classWriter.getTypeElement(),
                writerFactory.getEnumConstantWriter(classWriter));
    }

    /**
     * Return an instance of the field builder for the given class.
     *
     * @return an instance of the field builder for the given class.
     */
    public AbstractBuilder getFieldBuilder(ClassWriter classWriter) {
        return FieldBuilder.getInstance(context, classWriter.getTypeElement(),
            writerFactory.getFieldWriter(classWriter));
    }

    /**
     * Return an instance of the property builder for the given class.
     *
     * @return an instance of the field builder for the given class.
     */
    public AbstractBuilder getPropertyBuilder(ClassWriter classWriter) {
        final PropertyWriter propertyWriter =
                writerFactory.getPropertyWriter(classWriter);
        return PropertyBuilder.getInstance(context,
                                           classWriter.getTypeElement(),
                                           propertyWriter);
    }

    /**
     * Return an instance of the constructor builder for the given class.
     *
     * @return an instance of the constructor builder for the given class.
     */
    public AbstractBuilder getConstructorBuilder(ClassWriter classWriter) {
        return ConstructorBuilder.getInstance(context, classWriter.getTypeElement(),
            writerFactory.getConstructorWriter(classWriter));
    }

    /**
     * Return an instance of the member summary builder for the given class.
     *
     * @return an instance of the member summary builder for the given class.
     */
    public AbstractBuilder getMemberSummaryBuilder(ClassWriter classWriter) {
        return MemberSummaryBuilder.getInstance(classWriter, context);
    }

    /**
     * Return an instance of the member summary builder for the given annotation
     * type.
     *
     * @return an instance of the member summary builder for the given
     *         annotation type.
     */
    public AbstractBuilder getMemberSummaryBuilder(AnnotationTypeWriter annotationTypeWriter) {
        return MemberSummaryBuilder.getInstance(annotationTypeWriter, context);
    }

    /**
     * Return the builder that builds the serialized form.
     *
     * @return the builder that builds the serialized form.
     */
    public AbstractBuilder getSerializedFormBuilder() {
        return SerializedFormBuilder.getInstance(context);
    }
}
