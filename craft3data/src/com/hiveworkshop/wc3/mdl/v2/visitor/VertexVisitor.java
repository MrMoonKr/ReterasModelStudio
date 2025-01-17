package com.hiveworkshop.wc3.mdl.v2.visitor;

/**
 * 하나의 정점 처리를 위한 방문자 패턴의 인터페이스입니다.
 */
public interface VertexVisitor {

    void textureCoords( double u, double v );

    void vertexFinished();

    public static final VertexVisitor NO_ACTION = new VertexVisitor() {

        @Override
        public void textureCoords( final double u, final double v ) {
        }

        @Override
        public void vertexFinished() {
        }
    };
}
