package com.example.soneumproject.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUnityFunction is a Querydsl query type for UnityFunction
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUnityFunction extends EntityPathBase<UnityFunction> {

    private static final long serialVersionUID = -422059725L;

    public static final QUnityFunction unityFunction = new QUnityFunction("unityFunction");

    public final DateTimePath<java.util.Date> editDate = createDateTime("editDate", java.util.Date.class);

    public final StringPath editor = createString("editor");

    public final StringPath functionName = createString("functionName");

    public final NumberPath<Integer> functionNumber = createNumber("functionNumber", Integer.class);

    public final StringPath koreanWord = createString("koreanWord");

    public QUnityFunction(String variable) {
        super(UnityFunction.class, forVariable(variable));
    }

    public QUnityFunction(Path<? extends UnityFunction> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUnityFunction(PathMetadata metadata) {
        super(UnityFunction.class, metadata);
    }

}

