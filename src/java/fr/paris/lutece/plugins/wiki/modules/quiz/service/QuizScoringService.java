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
package fr.paris.lutece.plugins.wiki.modules.quiz.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizAnswer;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizAnswerHome;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizQuestion;
import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * Service class responsible for scoring quiz questions and calculating results. Supports multiple question types including MCQ, True/False, Matching, and
 * Ordering.
 */
public final class QuizScoringService
{
    // Constants
    private static final String ANSWER_SEPARATOR = ",";
    private static final int PERCENTAGE_MULTIPLIER = 100;
    private static final int DEFAULT_PERCENTAGE = 0;

    // Log messages
    private static final String LOG_ERROR_PARSING_MATCHING = "Error parsing matching answer: ";
    private static final String LOG_ERROR_PARSING_ORDERING = "Error parsing ordering answer: ";

    // Object mapper instance
    private static final ObjectMapper _objectMapper = new ObjectMapper( );

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private QuizScoringService( )
    {
    }

    /**
     * Scores a quiz question based on the user's answer.
     *
     * @param question
     *            the quiz question to score
     * @param strUserAnswer
     *            the user's answer as a string
     * @return a ScoringResult containing whether the answer was correct and points earned
     */
    public static ScoringResult scoreQuestion( QuizQuestion question, String strUserAnswer )
    {
        List<QuizAnswer> answers = QuizAnswerHome.getAnswersByQuestion( question.getId( ) );
        question.setAnswers( answers );

        boolean bIsCorrect = false;
        int nPointsEarned = 0;

        switch( question.getQuestionType( ) )
        {
            case MCQ:
                bIsCorrect = scoreMCQ( question, strUserAnswer );
                break;
            case TRUE_FALSE:
                bIsCorrect = scoreTrueFalse( question, strUserAnswer );
                break;
            case MATCHING:
                bIsCorrect = scoreMatching( question, strUserAnswer );
                break;
            case ORDERING:
                bIsCorrect = scoreOrdering( question, strUserAnswer );
                break;
        }

        if ( bIsCorrect )
        {
            nPointsEarned = question.getPoints( );
        }

        return new ScoringResult( bIsCorrect, nPointsEarned );
    }

    /**
     * Calculates the total possible points for a list of questions.
     *
     * @param questions
     *            the list of quiz questions
     * @return the sum of all question points
     */
    public static int calculateTotalPoints( List<QuizQuestion> questions )
    {
        return questions.stream( ).mapToInt( QuizQuestion::getPoints ).sum( );
    }

    /**
     * Calculates the percentage score from earned points and total points.
     *
     * @param nEarnedPoints
     *            the number of points earned
     * @param nTotalPoints
     *            the total possible points
     * @return the percentage score (0-100), or 0 if total points is zero
     */
    public static int calculatePercentageScore( int nEarnedPoints, int nTotalPoints )
    {
        if ( nTotalPoints == 0 )
        {
            return DEFAULT_PERCENTAGE;
        }
        return ( nEarnedPoints * PERCENTAGE_MULTIPLIER ) / nTotalPoints;
    }

    /**
     * Scores a multiple choice question by comparing user-selected answer IDs with correct answer IDs.
     *
     * @param question
     *            the MCQ question to score
     * @param strUserAnswer
     *            comma-separated list of selected answer IDs
     * @return true if the user's selection exactly matches the correct answers, false otherwise
     */
    private static boolean scoreMCQ( QuizQuestion question, String strUserAnswer )
    {
        if ( strUserAnswer == null || strUserAnswer.isEmpty( ) )
        {
            return false;
        }

        Set<Integer> correctAnswerIds = question.getAnswers( ).stream( ).filter( QuizAnswer::getIsCorrect ).map( QuizAnswer::getId )
                .collect( Collectors.toSet( ) );

        Set<Integer> userAnswerIds;
        try
        {
            userAnswerIds = Arrays.stream( strUserAnswer.split( ANSWER_SEPARATOR ) ).map( String::trim ).filter( s -> !s.isEmpty( ) ).map( Integer::parseInt )
                    .collect( Collectors.toSet( ) );
        }
        catch( NumberFormatException e )
        {
            return false;
        }

        return correctAnswerIds.equals( userAnswerIds );
    }

    /**
     * Scores a true/false question by comparing the user's answer ID with the correct answer ID.
     *
     * @param question
     *            the true/false question to score
     * @param strUserAnswer
     *            the ID of the selected answer as a string
     * @return true if the user selected the correct answer, false otherwise
     */
    private static boolean scoreTrueFalse( QuizQuestion question, String strUserAnswer )
    {
        if ( strUserAnswer == null || strUserAnswer.isEmpty( ) )
        {
            return false;
        }

        QuizAnswer correctAnswer = question.getAnswers( ).stream( ).filter( QuizAnswer::getIsCorrect ).findFirst( ).orElse( null );

        if ( correctAnswer == null )
        {
            return false;
        }

        try
        {
            int nUserAnswerId = Integer.parseInt( strUserAnswer.trim( ) );
            return correctAnswer.getId( ) == nUserAnswerId;
        }
        catch( NumberFormatException e )
        {
            return false;
        }
    }

    /**
     * Scores a matching question by comparing user matches with correct match targets.
     *
     * @param question
     *            the matching question to score
     * @param strUserAnswer
     *            JSON string representing a map of answer IDs to user-matched targets
     * @return true if all user matches are correct, false otherwise
     */
    private static boolean scoreMatching( QuizQuestion question, String strUserAnswer )
    {
        if ( strUserAnswer == null || strUserAnswer.isEmpty( ) )
        {
            return false;
        }

        try
        {
            Map<String, String> userMatches = _objectMapper.readValue( strUserAnswer, new TypeReference<Map<String, String>>( )
            {
            } );

            for ( QuizAnswer answer : question.getAnswers( ) )
            {
                String strMatchTarget = answer.getMatchTarget( );
                if ( strMatchTarget == null || strMatchTarget.isEmpty( ) )
                {
                    continue;
                }

                String strUserMatch = userMatches.get( String.valueOf( answer.getId( ) ) );
                if ( strUserMatch == null || !strUserMatch.equals( strMatchTarget ) )
                {
                    return false;
                }
            }

            return true;
        }
        catch( JsonProcessingException e )
        {
            AppLogService.error( "{}{}", LOG_ERROR_PARSING_MATCHING, e.getMessage( ), e );
            return false;
        }
    }

    /**
     * Scores an ordering question by comparing the user's order with the correct order.
     *
     * @param question
     *            the ordering question to score
     * @param strUserAnswer
     *            JSON string representing a list of answer IDs in user-specified order
     * @return true if the user's order matches the correct order, false otherwise
     */
    private static boolean scoreOrdering( QuizQuestion question, String strUserAnswer )
    {
        if ( strUserAnswer == null || strUserAnswer.isEmpty( ) )
        {
            return false;
        }

        try
        {
            List<Integer> userOrder = _objectMapper.readValue( strUserAnswer, new TypeReference<List<Integer>>( )
            {
            } );

            List<QuizAnswer> sortedAnswers = question.getAnswers( ).stream( ).filter( a -> a.getCorrectOrder( ) != null )
                    .sorted( ( a, b ) -> a.getCorrectOrder( ).compareTo( b.getCorrectOrder( ) ) ).collect( Collectors.toList( ) );

            if ( userOrder.size( ) != sortedAnswers.size( ) )
            {
                return false;
            }

            for ( int i = 0; i < sortedAnswers.size( ); i++ )
            {
                if ( sortedAnswers.get( i ).getId( ) != userOrder.get( i ) )
                {
                    return false;
                }
            }

            return true;
        }
        catch( JsonProcessingException e )
        {
            AppLogService.error( "{}{}", LOG_ERROR_PARSING_ORDERING, e.getMessage( ), e );
            return false;
        }
    }

    /**
     * Inner class representing the result of scoring a quiz question.
     */
    public static class ScoringResult
    {
        private final boolean _bIsCorrect;
        private final int _nPointsEarned;

        /**
         * Constructs a new ScoringResult.
         *
         * @param bIsCorrect
         *            whether the answer was correct
         * @param nPointsEarned
         *            the number of points earned
         */
        public ScoringResult( boolean bIsCorrect, int nPointsEarned )
        {
            this._bIsCorrect = bIsCorrect;
            this._nPointsEarned = nPointsEarned;
        }

        /**
         * Returns whether the answer was correct.
         *
         * @return true if the answer was correct, false otherwise
         */
        public boolean isCorrect( )
        {
            return _bIsCorrect;
        }

        /**
         * Returns the number of points earned.
         *
         * @return the points earned
         */
        public int getPointsEarned( )
        {
            return _nPointsEarned;
        }
    }
}
