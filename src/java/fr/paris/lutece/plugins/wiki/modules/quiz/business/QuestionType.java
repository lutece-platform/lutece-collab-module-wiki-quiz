/*
 * Copyright (c) 2002-2026, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.wiki.modules.quiz.business;

/**
 * Enumeration of different quiz question types.
 */
public enum QuestionType
{
    /** Multiple choice question type */
    MCQ( "MCQ" ),
    /** True or false question type */
    TRUE_FALSE( "TRUE_FALSE" ),
    /** Matching question type */
    MATCHING( "MATCHING" ),
    /** Ordering question type */
    ORDERING( "ORDERING" );

    private final String _strCode;

    /**
     * Constructs a question type with the specified code.
     *
     * @param strCode
     *            The code representing the question type
     */
    QuestionType( String strCode )
    {
        this._strCode = strCode;
    }

    /**
     * Gets the code of this question type.
     *
     * @return The question type code
     */
    public String getCode( )
    {
        return _strCode;
    }

    /**
     * Retrieves a question type from its code.
     *
     * @param strCode
     *            The question type code
     * @return The matching question type, or MCQ if not found
     */
    public static QuestionType fromCode( String strCode )
    {
        for ( QuestionType type : values( ) )
        {
            if ( type.getCode( ).equalsIgnoreCase( strCode ) )
            {
                return type;
            }
        }
        return MCQ;
    }
}
