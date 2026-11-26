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

/**
 * Quiz response entity representing a user's response to a question in an attempt.
 */
public class QuizResponse implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int _nId;
    private int _nIdAttempt;
    private int _nIdQuestion;
    private String _strUserAnswer;
    private Boolean _bIsCorrect;
    private int _nPointsEarned;

    /**
     * Default constructor.
     */
    public QuizResponse( )
    {
    }

    /**
     * Returns the response identifier.
     *
     * @return the response identifier
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the response identifier.
     *
     * @param nId
     *            the response identifier to set
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the attempt identifier.
     *
     * @return the attempt identifier
     */
    public int getIdAttempt( )
    {
        return _nIdAttempt;
    }

    /**
     * Sets the attempt identifier.
     *
     * @param nIdAttempt
     *            the attempt identifier to set
     */
    public void setIdAttempt( int nIdAttempt )
    {
        _nIdAttempt = nIdAttempt;
    }

    /**
     * Returns the question identifier.
     *
     * @return the question identifier
     */
    public int getIdQuestion( )
    {
        return _nIdQuestion;
    }

    /**
     * Sets the question identifier.
     *
     * @param nIdQuestion
     *            the question identifier to set
     */
    public void setIdQuestion( int nIdQuestion )
    {
        _nIdQuestion = nIdQuestion;
    }

    /**
     * Returns the user's answer.
     *
     * @return the user's answer
     */
    public String getUserAnswer( )
    {
        return _strUserAnswer;
    }

    /**
     * Sets the user's answer.
     *
     * @param strUserAnswer
     *            the user's answer to set
     */
    public void setUserAnswer( String strUserAnswer )
    {
        _strUserAnswer = strUserAnswer;
    }

    /**
     * Returns whether the response is correct.
     *
     * @return true if correct, false otherwise, null if not evaluated
     */
    public Boolean getIsCorrect( )
    {
        return _bIsCorrect;
    }

    /**
     * Sets whether the response is correct.
     *
     * @param bIsCorrect
     *            true if correct, false otherwise
     */
    public void setIsCorrect( Boolean bIsCorrect )
    {
        _bIsCorrect = bIsCorrect;
    }

    /**
     * Returns the points earned for this response.
     *
     * @return the points earned
     */
    public int getPointsEarned( )
    {
        return _nPointsEarned;
    }

    /**
     * Sets the points earned for this response.
     *
     * @param nPointsEarned
     *            the points earned to set
     */
    public void setPointsEarned( int nPointsEarned )
    {
        _nPointsEarned = nPointsEarned;
    }
}
