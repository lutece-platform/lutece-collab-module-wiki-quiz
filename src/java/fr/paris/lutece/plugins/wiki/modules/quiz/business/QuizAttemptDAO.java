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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of quiz attempt data access object.
 */
@ApplicationScoped
public class QuizAttemptDAO implements IQuizAttemptDAO
{
    private static final String SQL_QUERY_SELECT = "SELECT id_attempt, id_quiz, user_guid, score, passed, started_at, completed_at FROM wiki_quiz_attempt WHERE id_attempt = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO wiki_quiz_attempt ( id_quiz, user_guid, score, passed, started_at, completed_at ) VALUES ( ?, ?, ?, ?, ?, ? )";
    private static final String SQL_QUERY_UPDATE = "UPDATE wiki_quiz_attempt SET id_quiz = ?, user_guid = ?, score = ?, passed = ?, started_at = ?, completed_at = ? WHERE id_attempt = ?";
    private static final String SQL_QUERY_SELECT_BY_QUIZ = "SELECT id_attempt, id_quiz, user_guid, score, passed, started_at, completed_at FROM wiki_quiz_attempt WHERE id_quiz = ? ORDER BY started_at DESC";
    private static final String SQL_QUERY_SELECT_BY_QUIZ_AND_USER = "SELECT id_attempt, id_quiz, user_guid, score, passed, started_at, completed_at FROM wiki_quiz_attempt WHERE id_quiz = ? AND user_guid = ? ORDER BY started_at DESC";
    private static final String SQL_QUERY_COUNT_BY_QUIZ_AND_USER = "SELECT COUNT(*) FROM wiki_quiz_attempt WHERE id_quiz = ? AND user_guid = ?";

    private static final String COLUMN_ID_ATTEMPT = "id_attempt";
    private static final String COLUMN_ID_QUIZ = "id_quiz";
    private static final String COLUMN_USER_GUID = "user_guid";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_PASSED = "passed";
    private static final String COLUMN_STARTED_AT = "started_at";
    private static final String COLUMN_COMPLETED_AT = "completed_at";

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( QuizAttempt attempt, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, attempt.getIdQuiz( ) );
            daoUtil.setString( nIndex++, attempt.getUserGuid( ) );

            if ( attempt.getScore( ) != null )
            {
                daoUtil.setInt( nIndex++, attempt.getScore( ) );
            }
            else
            {
                daoUtil.setIntNull( nIndex++ );
            }

            if ( attempt.getPassed( ) != null )
            {
                daoUtil.setBoolean( nIndex++, attempt.getPassed( ) );
            }
            else
            {
                daoUtil.setIntNull( nIndex++ );
            }

            Timestamp now = new Timestamp( Calendar.getInstance( ).getTimeInMillis( ) );
            daoUtil.setTimestamp( nIndex++, attempt.getStartedAt( ) != null ? attempt.getStartedAt( ) : now );

            if ( attempt.getCompletedAt( ) != null )
            {
                daoUtil.setTimestamp( nIndex++, attempt.getCompletedAt( ) );
            }
            else
            {
                daoUtil.setTimestamp( nIndex++, null );
            }

            daoUtil.executeUpdate( );

            if ( daoUtil.nextGeneratedKey( ) )
            {
                attempt.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<QuizAttempt> load( int nKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );

            QuizAttempt attempt = null;

            if ( daoUtil.next( ) )
            {
                attempt = dataToObject( daoUtil );
            }

            return Optional.ofNullable( attempt );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( QuizAttempt attempt, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, attempt.getIdQuiz( ) );
            daoUtil.setString( nIndex++, attempt.getUserGuid( ) );

            if ( attempt.getScore( ) != null )
            {
                daoUtil.setInt( nIndex++, attempt.getScore( ) );
            }
            else
            {
                daoUtil.setIntNull( nIndex++ );
            }

            if ( attempt.getPassed( ) != null )
            {
                daoUtil.setBoolean( nIndex++, attempt.getPassed( ) );
            }
            else
            {
                daoUtil.setIntNull( nIndex++ );
            }

            daoUtil.setTimestamp( nIndex++, attempt.getStartedAt( ) );

            if ( attempt.getCompletedAt( ) != null )
            {
                daoUtil.setTimestamp( nIndex++, attempt.getCompletedAt( ) );
            }
            else
            {
                daoUtil.setTimestamp( nIndex++, null );
            }

            daoUtil.setInt( nIndex, attempt.getId( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<QuizAttempt> selectByQuiz( int nIdQuiz, Plugin plugin )
    {
        List<QuizAttempt> attemptList = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_QUIZ, plugin ) )
        {
            daoUtil.setInt( 1, nIdQuiz );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                attemptList.add( dataToObject( daoUtil ) );
            }
        }

        return attemptList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<QuizAttempt> selectByQuizAndUser( int nIdQuiz, String strUserGuid, Plugin plugin )
    {
        List<QuizAttempt> attemptList = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_QUIZ_AND_USER, plugin ) )
        {
            daoUtil.setInt( 1, nIdQuiz );
            daoUtil.setString( 2, strUserGuid );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                attemptList.add( dataToObject( daoUtil ) );
            }
        }

        return attemptList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countByQuizAndUser( int nIdQuiz, String strUserGuid, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COUNT_BY_QUIZ_AND_USER, plugin ) )
        {
            daoUtil.setInt( 1, nIdQuiz );
            daoUtil.setString( 2, strUserGuid );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                return daoUtil.getInt( 1 );
            }

            return 0;
        }
    }

    /**
     * Converts database row data to a QuizAttempt object.
     *
     * @param daoUtil
     *            The data access utility
     * @return The quiz attempt object
     */
    private QuizAttempt dataToObject( DAOUtil daoUtil )
    {
        QuizAttempt attempt = new QuizAttempt( );

        attempt.setId( daoUtil.getInt( COLUMN_ID_ATTEMPT ) );
        attempt.setIdQuiz( daoUtil.getInt( COLUMN_ID_QUIZ ) );
        attempt.setUserGuid( daoUtil.getString( COLUMN_USER_GUID ) );
        attempt.setScore( daoUtil.getObject( COLUMN_SCORE, Integer.class ) );
        attempt.setPassed( daoUtil.getObject( COLUMN_PASSED, Boolean.class ) );
        attempt.setStartedAt( daoUtil.getTimestamp( COLUMN_STARTED_AT ) );
        attempt.setCompletedAt( daoUtil.getTimestamp( COLUMN_COMPLETED_AT ) );

        return attempt;
    }
}
