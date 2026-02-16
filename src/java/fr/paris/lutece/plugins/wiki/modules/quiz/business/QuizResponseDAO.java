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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import jakarta.enterprise.context.ApplicationScoped;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of quiz response data access object.
 */
@ApplicationScoped
public class QuizResponseDAO implements IQuizResponseDAO
{
    private static final String SQL_QUERY_INSERT = "INSERT INTO wiki_quiz_response ( id_attempt, id_question, user_answer, is_correct, points_earned ) VALUES ( ?, ?, ?, ?, ? )";
    private static final String SQL_QUERY_UPDATE = "UPDATE wiki_quiz_response SET id_attempt = ?, id_question = ?, user_answer = ?, is_correct = ?, points_earned = ? WHERE id_response = ?";
    private static final String SQL_QUERY_SELECT_BY_ATTEMPT = "SELECT id_response, id_attempt, id_question, user_answer, is_correct, points_earned FROM wiki_quiz_response WHERE id_attempt = ?";
    private static final String SQL_QUERY_SELECT_BY_ATTEMPT_AND_QUESTION = "SELECT id_response, id_attempt, id_question, user_answer, is_correct, points_earned FROM wiki_quiz_response WHERE id_attempt = ? AND id_question = ?";

    private static final String COLUMN_ID_RESPONSE = "id_response";
    private static final String COLUMN_ID_ATTEMPT = "id_attempt";
    private static final String COLUMN_ID_QUESTION = "id_question";
    private static final String COLUMN_USER_ANSWER = "user_answer";
    private static final String COLUMN_IS_CORRECT = "is_correct";
    private static final String COLUMN_POINTS_EARNED = "points_earned";

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( QuizResponse response, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, response.getIdAttempt( ) );
            daoUtil.setInt( nIndex++, response.getIdQuestion( ) );
            daoUtil.setString( nIndex++, response.getUserAnswer( ) );

            if ( response.getIsCorrect( ) != null )
            {
                daoUtil.setBoolean( nIndex++, response.getIsCorrect( ) );
            }
            else
            {
                daoUtil.setIntNull( nIndex++ );
            }

            daoUtil.setInt( nIndex++, response.getPointsEarned( ) );

            daoUtil.executeUpdate( );

            if ( daoUtil.nextGeneratedKey( ) )
            {
                response.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( QuizResponse response, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, response.getIdAttempt( ) );
            daoUtil.setInt( nIndex++, response.getIdQuestion( ) );
            daoUtil.setString( nIndex++, response.getUserAnswer( ) );

            if ( response.getIsCorrect( ) != null )
            {
                daoUtil.setBoolean( nIndex++, response.getIsCorrect( ) );
            }
            else
            {
                daoUtil.setIntNull( nIndex++ );
            }

            daoUtil.setInt( nIndex++, response.getPointsEarned( ) );

            daoUtil.setInt( nIndex, response.getId( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<QuizResponse> selectByAttempt( int nIdAttempt, Plugin plugin )
    {
        List<QuizResponse> responseList = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ATTEMPT, plugin ) )
        {
            daoUtil.setInt( 1, nIdAttempt );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                responseList.add( dataToObject( daoUtil ) );
            }
        }

        return responseList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<QuizResponse> selectByAttemptAndQuestion( int nIdAttempt, int nIdQuestion, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ATTEMPT_AND_QUESTION, plugin ) )
        {
            daoUtil.setInt( 1, nIdAttempt );
            daoUtil.setInt( 2, nIdQuestion );
            daoUtil.executeQuery( );

            QuizResponse response = null;

            if ( daoUtil.next( ) )
            {
                response = dataToObject( daoUtil );
            }

            return Optional.ofNullable( response );
        }
    }

    /**
     * Converts database row data to a QuizResponse object.
     *
     * @param daoUtil
     *            The data access utility
     * @return The quiz response object
     */
    private QuizResponse dataToObject( DAOUtil daoUtil )
    {
        QuizResponse response = new QuizResponse( );

        response.setId( daoUtil.getInt( COLUMN_ID_RESPONSE ) );
        response.setIdAttempt( daoUtil.getInt( COLUMN_ID_ATTEMPT ) );
        response.setIdQuestion( daoUtil.getInt( COLUMN_ID_QUESTION ) );
        response.setUserAnswer( daoUtil.getString( COLUMN_USER_ANSWER ) );
        response.setIsCorrect( daoUtil.getObject( COLUMN_IS_CORRECT, Boolean.class ) );
        response.setPointsEarned( daoUtil.getInt( COLUMN_POINTS_EARNED ) );

        return response;
    }
}
