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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Quiz question entity representing a question within a quiz.
 */
public class QuizQuestion implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int _nId;
    private int _nIdQuiz;

    @NotNull( message = "#i18n{module.wiki.quiz.validation.quizQuestion.Type.notEmpty}" )
    private QuestionType _questionType = QuestionType.MCQ;

    @NotEmpty( message = "#i18n{module.wiki.quiz.validation.quizQuestion.Text.notEmpty}" )
    private String _strQuestionText;

    private String _strExplanation;
    private int _nPoints = 1;
    private int _nDisplayOrder;

    private transient List<QuizAnswer> _listAnswers;
    private transient List<Integer> _listSourcePageIds;

    /**
     * Default constructor.
     */
    public QuizQuestion( )
    {
    }

    /**
     * Gets the question ID.
     *
     * @return The question ID
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the question ID.
     *
     * @param nId
     *            The question ID
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Gets the associated quiz ID.
     *
     * @return The quiz ID
     */
    public int getIdQuiz( )
    {
        return _nIdQuiz;
    }

    /**
     * Sets the associated quiz ID.
     *
     * @param nIdQuiz
     *            The quiz ID
     */
    public void setIdQuiz( int nIdQuiz )
    {
        _nIdQuiz = nIdQuiz;
    }

    /**
     * Gets the question type.
     *
     * @return The question type
     */
    public QuestionType getQuestionType( )
    {
        return _questionType;
    }

    /**
     * Sets the question type.
     *
     * @param questionType
     *            The question type
     */
    public void setQuestionType( QuestionType questionType )
    {
        _questionType = questionType;
    }

    /**
     * Sets the question type from a code string.
     *
     * @param strQuestionType
     *            The question type code
     */
    public void setQuestionTypeCode( String strQuestionType )
    {
        if ( strQuestionType != null && !strQuestionType.isEmpty( ) )
        {
            _questionType = QuestionType.fromCode( strQuestionType );
        }
    }

    /**
     * Gets the question text.
     *
     * @return The question text
     */
    public String getQuestionText( )
    {
        return _strQuestionText;
    }

    /**
     * Sets the question text.
     *
     * @param strQuestionText
     *            The question text
     */
    public void setQuestionText( String strQuestionText )
    {
        _strQuestionText = strQuestionText;
    }

    /**
     * Gets the question explanation.
     *
     * @return The explanation
     */
    public String getExplanation( )
    {
        return _strExplanation;
    }

    /**
     * Sets the question explanation.
     *
     * @param strExplanation
     *            The explanation
     */
    public void setExplanation( String strExplanation )
    {
        _strExplanation = strExplanation;
    }

    /**
     * Gets the point value for this question.
     *
     * @return The points
     */
    public int getPoints( )
    {
        return _nPoints;
    }

    /**
     * Sets the point value for this question.
     *
     * @param nPoints
     *            The points
     */
    public void setPoints( int nPoints )
    {
        _nPoints = nPoints;
    }

    /**
     * Gets the display order of this question.
     *
     * @return The display order
     */
    public int getDisplayOrder( )
    {
        return _nDisplayOrder;
    }

    /**
     * Sets the display order of this question.
     *
     * @param nDisplayOrder
     *            The display order
     */
    public void setDisplayOrder( int nDisplayOrder )
    {
        _nDisplayOrder = nDisplayOrder;
    }

    /**
     * Gets the list of answers for this question.
     *
     * @return The list of answers
     */
    public List<QuizAnswer> getAnswers( )
    {
        if ( _listAnswers == null )
        {
            _listAnswers = new ArrayList<>( );
        }
        return _listAnswers;
    }

    /**
     * Sets the list of answers for this question.
     *
     * @param listAnswers
     *            The list of answers
     */
    public void setAnswers( List<QuizAnswer> listAnswers )
    {
        _listAnswers = listAnswers;
    }

    /**
     * Gets the list of source page IDs for this question.
     *
     * @return The list of source page IDs
     */
    public List<Integer> getSourcePageIds( )
    {
        if ( _listSourcePageIds == null )
        {
            _listSourcePageIds = new ArrayList<>( );
        }
        return _listSourcePageIds;
    }

    /**
     * Sets the list of source page IDs for this question.
     *
     * @param listSourcePageIds
     *            The list of source page IDs
     */
    public void setSourcePageIds( List<Integer> listSourcePageIds )
    {
        _listSourcePageIds = listSourcePageIds != null ? listSourcePageIds : new ArrayList<>( );
    }
}
