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

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * Quiz answer entity representing a possible answer to a quiz question.
 */
public class QuizAnswer implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int _nId;
    private int _nIdQuestion;

    @NotEmpty( message = "#i18n{module.wiki.quiz.validation.quizAnswer.Text.notEmpty}" )
    @Size( max = 1000, message = "#i18n{module.wiki.quiz.validation.quizAnswer.Text.size}" )
    private String _strAnswerText;

    private boolean _bIsCorrect;

    @Size( max = 1000, message = "#i18n{module.wiki.quiz.validation.quizAnswer.MatchTarget.size}" )
    private String _strMatchTarget;

    private Integer _nCorrectOrder;
    private int _nDisplayOrder;

    /**
     * Default constructor.
     */
    public QuizAnswer( )
    {
    }

    /**
     * Gets the answer ID.
     *
     * @return The answer ID
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the answer ID.
     *
     * @param nId
     *            The answer ID
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Gets the associated question ID.
     *
     * @return The question ID
     */
    public int getIdQuestion( )
    {
        return _nIdQuestion;
    }

    /**
     * Sets the associated question ID.
     *
     * @param nIdQuestion
     *            The question ID
     */
    public void setIdQuestion( int nIdQuestion )
    {
        _nIdQuestion = nIdQuestion;
    }

    /**
     * Gets the answer text.
     *
     * @return The answer text
     */
    public String getAnswerText( )
    {
        return _strAnswerText;
    }

    /**
     * Sets the answer text.
     *
     * @param strAnswerText
     *            The answer text
     */
    public void setAnswerText( String strAnswerText )
    {
        _strAnswerText = strAnswerText;
    }

    /**
     * Gets whether this answer is correct.
     *
     * @return True if the answer is correct
     */
    public boolean getIsCorrect( )
    {
        return _bIsCorrect;
    }

    /**
     * Sets whether this answer is correct.
     *
     * @param bIsCorrect
     *            True if the answer is correct
     */
    public void setIsCorrect( boolean bIsCorrect )
    {
        _bIsCorrect = bIsCorrect;
    }

    /**
     * Gets the match target for matching questions.
     *
     * @return The match target
     */
    public String getMatchTarget( )
    {
        return _strMatchTarget;
    }

    /**
     * Sets the match target for matching questions.
     *
     * @param strMatchTarget
     *            The match target
     */
    public void setMatchTarget( String strMatchTarget )
    {
        _strMatchTarget = strMatchTarget;
    }

    /**
     * Gets the correct order for ordering questions.
     *
     * @return The correct order
     */
    public Integer getCorrectOrder( )
    {
        return _nCorrectOrder;
    }

    /**
     * Sets the correct order for ordering questions.
     *
     * @param nCorrectOrder
     *            The correct order
     */
    public void setCorrectOrder( Integer nCorrectOrder )
    {
        _nCorrectOrder = nCorrectOrder;
    }

    /**
     * Gets the display order of this answer.
     *
     * @return The display order
     */
    public int getDisplayOrder( )
    {
        return _nDisplayOrder;
    }

    /**
     * Sets the display order of this answer.
     *
     * @param nDisplayOrder
     *            The display order
     */
    public void setDisplayOrder( int nDisplayOrder )
    {
        _nDisplayOrder = nDisplayOrder;
    }
}
