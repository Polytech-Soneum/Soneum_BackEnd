package com.example.soneumproject.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUsers is a Querydsl query type for Users
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUsers extends EntityPathBase<Users> {

    private static final long serialVersionUID = -1096897842L;

    public static final QUsers users = new QUsers("users");

    public final NumberPath<Integer> userDisabilityStatus = createNumber("userDisabilityStatus", Integer.class);

    public final StringPath userEmail = createString("userEmail");

    public final NumberPath<Integer> userGender = createNumber("userGender", Integer.class);

    public final EnumPath<Grade> userGrade = createEnum("userGrade", Grade.class);

    public final StringPath userID = createString("userID");

    public final StringPath userNickname = createString("userNickname");

    public final StringPath userPassword = createString("userPassword");

    public final DateTimePath<java.time.LocalDateTime> userRecentLogin = createDateTime("userRecentLogin", java.time.LocalDateTime.class);

    public QUsers(String variable) {
        super(Users.class, forVariable(variable));
    }

    public QUsers(Path<? extends Users> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUsers(PathMetadata metadata) {
        super(Users.class, metadata);
    }

}

