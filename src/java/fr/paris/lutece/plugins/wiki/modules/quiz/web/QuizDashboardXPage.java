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
package fr.paris.lutece.plugins.wiki.modules.quiz.web;

import fr.paris.lutece.plugins.wiki.business.item.AbstractWikiItem;
import fr.paris.lutece.plugins.wiki.business.item.WikiItemHome;
import fr.paris.lutece.plugins.wiki.business.item.impl.Book;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.Quiz;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizAttempt;
import fr.paris.lutece.plugins.wiki.modules.quiz.business.QuizAttemptHome;
import fr.paris.lutece.plugins.wiki.modules.quiz.service.QuizService;
import fr.paris.lutece.plugins.wiki.service.security.WikiAccessControlService;
import fr.paris.lutece.plugins.wiki.web.AbstractWikiXPage;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.html.AbstractPaginator;
import fr.paris.lutece.util.url.UrlItem;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * XPage controller for the quiz dashboard. Provides views for displaying quiz statistics and managing quiz attempts.
 */
@SessionScoped
@Named( "wiki-quiz.xpage.quizdashboard" )
@Controller( xpageName = "quizdashboard", pageTitleI18nKey = "module.wiki.quiz.xpage.quizDashboard.pageTitle", pagePathI18nKey = "module.wiki.quiz.xpage.quizDashboard.pageTitle" )
public class QuizDashboardXPage extends AbstractWikiXPage
{
    private static final long serialVersionUID = 1L;

    @Inject
    private Models _models;

    // Views
    private static final String VIEW_DASHBOARD = "dashboard";
    private static final String VIEW_QUIZ_STATS = "quizStats";

    // Templates
    private static final String TEMPLATE_DASHBOARD = "skin/plugins/wiki/modules/quiz/dashboard.html";
    private static final String TEMPLATE_QUIZ_STATS = "skin/plugins/wiki/modules/quiz/dashboard_quiz_stats.html";

    // Parameters
    private static final String PARAMETER_BOOK_ID = "book_id";
    private static final String PARAMETER_QUIZ_ID = "quiz_id";
    private static final String PARAMETER_SEARCH_USER = "search_user";

    // Marks
    private static final String MARK_QUIZZES = "quizzes";
    private static final String MARK_QUIZ = "quiz";
    private static final String MARK_ATTEMPTS = "attempts";
    private static final String MARK_STATS = "stats";
    private static final String MARK_QUIZ_STATS = "quiz_stats";
    private static final String MARK_GLOBAL_STATS = "global_stats";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_SEARCH_USER = "search_user";

    // Properties
    private static final String PROPERTY_ITEMS_PER_PAGE = "module.wiki.quiz.quizStats.itemsPerPage";
    private static final int DEFAULT_ITEMS_PER_PAGE = 20;

    // Session variables
    private int _nItemsPerPage;
    private String _strCurrentPageIndex;

    // Stats keys
    private static final String STATS_KEY_TOTAL_ATTEMPTS = "totalAttempts";
    private static final String STATS_KEY_COMPLETED_ATTEMPTS = "completedAttempts";
    private static final String STATS_KEY_PASSED_ATTEMPTS = "passedAttempts";
    private static final String STATS_KEY_AVG_SCORE = "avgScore";
    private static final String STATS_KEY_PASS_RATE = "passRate";
    private static final String STATS_KEY_TOTAL_QUIZZES = "totalQuizzes";
    private static final String STATS_KEY_TOTAL_PASSED = "totalPassed";

    // I18n keys
    private static final String I18N_ERROR_BOOK_REQUIRED = "module.wiki.quiz.quiz.error.bookRequired";
    private static final String I18N_ERROR_ACCESS_DENIED = "module.wiki.quiz.quiz.error.accessDenied";

    // Constants
    private static final double PERCENTAGE_MULTIPLIER = 100.0;

    /**
     * Displays the quiz dashboard view for a specific book. Shows all quizzes associated with the book along with their statistics.
     *
     * @param request
     *            the HTTP servlet request containing the book_id parameter
     * @return the XPage containing the dashboard view with quiz statistics
     * @throws UserNotSignedException
     *             if the user is not authenticated
     */
    @View( value = VIEW_DASHBOARD, defaultView = true )
    public XPage viewDashboard( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = checkAuthentication( request );

        String strBookId = request.getParameter( PARAMETER_BOOK_ID );
        if ( strBookId == null || strBookId.isEmpty( ) )
        {
            addError( I18N_ERROR_BOOK_REQUIRED, getLocale( request ) );
            return getXPage( TEMPLATE_DASHBOARD, getLocale( request ) );
        }

        int nBookId = Integer.parseInt( strBookId );
        Optional<AbstractWikiItem> optBook = WikiItemHome.findByPrimaryKey( nBookId );

        if ( optBook.isEmpty( ) || !( optBook.get( ) instanceof Book ) || !WikiAccessControlService.canEdit( user, optBook.get( ) ) )
        {
            addError( I18N_ERROR_ACCESS_DENIED, getLocale( request ) );
            return getXPage( TEMPLATE_DASHBOARD, getLocale( request ) );
        }

        Book book = (Book) optBook.get( );
        List<Quiz> quizzes = QuizService.getQuizzesByBook( nBookId );

        Map<String, Map<String, Object>> quizStats = new HashMap<>( );
        int totalAttempts = 0;
        int totalPassed = 0;
        double totalScore = 0;
        int completedAttempts = 0;

        for ( Quiz quiz : quizzes )
        {
            Map<String, Object> stats = calculateQuizStats( quiz.getId( ) );
            quizStats.put( String.valueOf( quiz.getId( ) ), stats );

            totalAttempts += ( (Number) stats.get( STATS_KEY_TOTAL_ATTEMPTS ) ).intValue( );
            totalPassed += ( (Number) stats.get( STATS_KEY_PASSED_ATTEMPTS ) ).intValue( );
            completedAttempts += ( (Number) stats.get( STATS_KEY_COMPLETED_ATTEMPTS ) ).intValue( );
            totalScore += ( (Number) stats.get( STATS_KEY_AVG_SCORE ) ).doubleValue( ) * ( (Number) stats.get( STATS_KEY_COMPLETED_ATTEMPTS ) ).intValue( );
        }

        Map<String, Object> globalStats = new HashMap<>( );
        globalStats.put( STATS_KEY_TOTAL_QUIZZES, quizzes.size( ) );
        globalStats.put( STATS_KEY_TOTAL_ATTEMPTS, totalAttempts );
        globalStats.put( STATS_KEY_TOTAL_PASSED, totalPassed );
        globalStats.put( STATS_KEY_AVG_SCORE, completedAttempts > 0 ? Math.round( totalScore / completedAttempts ) : 0 );
        globalStats.put( STATS_KEY_PASS_RATE, totalAttempts > 0 ? Math.round( ( totalPassed * PERCENTAGE_MULTIPLIER ) / completedAttempts ) : 0 );

        populateBookSidebarModel( _models, user, book );
        _models.put( MARK_QUIZZES, quizzes );
        _models.put( MARK_QUIZ_STATS, quizStats );
        _models.put( MARK_GLOBAL_STATS, globalStats );

        return getXPage( TEMPLATE_DASHBOARD, getLocale( request ) );
    }

    /**
     * Displays detailed statistics for a specific quiz. Shows all attempts and computed statistics for the quiz.
     *
     * @param request
     *            the HTTP servlet request containing the quiz_id parameter
     * @return the XPage containing the quiz statistics view
     * @throws UserNotSignedException
     *             if the user is not authenticated
     */
    @View( VIEW_QUIZ_STATS )
    public XPage viewQuizStats( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = checkAuthentication( request );

        String strQuizId = request.getParameter( PARAMETER_QUIZ_ID );
        if ( strQuizId == null || strQuizId.isEmpty( ) )
        {
            return redirectView( request, VIEW_DASHBOARD );
        }

        int nQuizId = Integer.parseInt( strQuizId );
        Quiz quiz = QuizService.findById( nQuizId );

        if ( quiz == null )
        {
            return redirectView( request, VIEW_DASHBOARD );
        }

        Optional<AbstractWikiItem> optBook = WikiItemHome.findByPrimaryKey( quiz.getIdBook( ) );
        if ( optBook.isEmpty( ) || !( optBook.get( ) instanceof Book ) || !WikiAccessControlService.canEdit( user, optBook.get( ) ) )
        {
            return redirectView( request, VIEW_DASHBOARD );
        }

        Book book = (Book) optBook.get( );
        List<QuizAttempt> attempts = QuizAttemptHome.getAttemptsByQuiz( nQuizId );
        Map<String, Object> stats = calculateQuizStats( nQuizId );

        // Search filter
        String strSearchUser = request.getParameter( PARAMETER_SEARCH_USER );
        if ( strSearchUser != null && !strSearchUser.trim( ).isEmpty( ) )
        {
            String searchLower = strSearchUser.toLowerCase( ).trim( );
            attempts = attempts.stream( ).filter( a -> a.getUserGuid( ) != null && a.getUserGuid( ).toLowerCase( ).contains( searchLower ) )
                    .collect( Collectors.toList( ) );
        }

        // Pagination
        _strCurrentPageIndex = AbstractPaginator.getPageIndex( request, AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        int nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_ITEMS_PER_PAGE, DEFAULT_ITEMS_PER_PAGE );
        _nItemsPerPage = AbstractPaginator.getItemsPerPage( request, AbstractPaginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, nDefaultItemsPerPage );

        UrlItem url = new UrlItem( "jsp/site/Portal.jsp" );
        url.addParameter( "page", "quizdashboard" );
        url.addParameter( "view", VIEW_QUIZ_STATS );
        url.addParameter( PARAMETER_QUIZ_ID, strQuizId );
        if ( strSearchUser != null && !strSearchUser.trim( ).isEmpty( ) )
        {
            url.addParameter( PARAMETER_SEARCH_USER, strSearchUser );
        }

        LocalizedPaginator<QuizAttempt> paginator = new LocalizedPaginator<>( attempts, _nItemsPerPage, url.getUrl( ), AbstractPaginator.PARAMETER_PAGE_INDEX,
                _strCurrentPageIndex, getLocale( request ) );

        populateBookSidebarModel( _models, user, book );
        _models.put( MARK_QUIZ, quiz );
        _models.put( MARK_ATTEMPTS, paginator.getPageItems( ) );
        _models.put( MARK_STATS, stats );
        _models.put( MARK_PAGINATOR, paginator );
        _models.put( MARK_NB_ITEMS_PER_PAGE, String.valueOf( _nItemsPerPage ) );
        _models.put( MARK_SEARCH_USER, strSearchUser != null ? strSearchUser : "" );

        return getXPage( TEMPLATE_QUIZ_STATS, getLocale( request ) );
    }

    /**
     * Calculates statistics for a specific quiz. Computes total attempts, completed attempts, passed attempts, average score and pass rate.
     *
     * @param nQuizId
     *            the identifier of the quiz
     * @return a map containing the computed statistics
     */
    private Map<String, Object> calculateQuizStats( int nQuizId )
    {
        List<QuizAttempt> attempts = QuizAttemptHome.getAttemptsByQuiz( nQuizId );

        int totalAttempts = attempts.size( );
        int completedAttempts = 0;
        int passedAttempts = 0;
        double totalScore = 0;

        for ( QuizAttempt attempt : attempts )
        {
            if ( attempt.isCompleted( ) )
            {
                completedAttempts++;
                if ( attempt.getScore( ) != null )
                {
                    totalScore += attempt.getScore( );
                }
                if ( Boolean.TRUE.equals( attempt.getPassed( ) ) )
                {
                    passedAttempts++;
                }
            }
        }

        Map<String, Object> stats = new HashMap<>( );
        stats.put( STATS_KEY_TOTAL_ATTEMPTS, totalAttempts );
        stats.put( STATS_KEY_COMPLETED_ATTEMPTS, completedAttempts );
        stats.put( STATS_KEY_PASSED_ATTEMPTS, passedAttempts );
        stats.put( STATS_KEY_AVG_SCORE, completedAttempts > 0 ? Math.round( totalScore / completedAttempts ) : 0 );
        stats.put( STATS_KEY_PASS_RATE, completedAttempts > 0 ? Math.round( ( passedAttempts * PERCENTAGE_MULTIPLIER ) / completedAttempts ) : 0 );

        return stats;
    }

    /**
     * Checks if the user is authenticated. Retrieves the registered user from the security service.
     *
     * @param request
     *            the HTTP servlet request
     * @return the authenticated LuteceUser
     * @throws UserNotSignedException
     *             if no user is authenticated
     */
    private LuteceUser checkAuthentication( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        if ( user == null )
        {
            throw new UserNotSignedException( );
        }
        return user;
    }
}
