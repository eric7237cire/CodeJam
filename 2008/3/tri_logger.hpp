//
// Copyright (c) 2005 - 2010
// Seweryn Habdank-Wojewodzki
//
// Distributed under the Boost Software License, Version 1.0.
// ( copy at http://www.boost.org/LICENSE_1_0.txt )

#ifndef LOGGER_HPP_INCLUDED
#define LOGGER_HPP_INCLUDED 1

#include <ostream>
#include <memory>

namespace trivial_logger
{
    namespace detail
    {
        class tri_logger_impl;
    }

    // main trivial logger class
    class tri_logger_t
    {
    public:
        tri_logger_t ();
        ~tri_logger_t ();
        
        /// return true if logger is activated
        static bool is_activated();

        /// activate and deactivate logger
        static void activate ( bool const activated );

        /// return reference to pointer to output stream
        static ::std::ostream *& ostream_ptr();

    private:
        static std::auto_ptr<detail::tri_logger_impl> tl_impl;

        // in this design logger should be noncopyable
        tri_logger_t ( tri_logger_t const & );
        tri_logger_t & operator= ( tri_logger_t const & );
    };

    // important funtion which helps solves
    // "static initialisation fiasco" problem
    // see:
    // 1. S. Habdank-Wojewodzki, "C++ Trivial Logger", Overload 77, Feb 2007, pp.19-23
    // 2. http://www.parashift.com/c++-faq-lite/ctors.html#faq-10.13
    // present solution is much better
    extern tri_logger_t & tri_logger();

    namespace detail
    {
        extern ::std::auto_ptr<tri_logger_t> tri_logger_out_ptr;
    }

} // namespace trival_logger

// macro prints variable name and its value to the logger stream
#define LOG(name) \
    do { if ( trivial_logger::tri_logger().is_activated() ){\
    *trivial_logger::tri_logger().ostream_ptr() << __FILE__ \
    << " [" << __LINE__ << "] : " << #name \
    << " = " << (name) << ::std::endl;} }while(false)

// macro prints value of the variable to the logger stream
// useful for printing constant strings
#define LOG_STR(str) \
    do { if ( trivial_logger::tri_logger().is_activated() ){\
    *trivial_logger::tri_logger().ostream_ptr() << __FILE__ \
    << " [" << __LINE__ << "] : " << str \
    << ::std::endl;} }while(false)

// namespace for the trivial logger
namespace trivial_logger
{
    // example how to create functions which operates on logger stream
    // here are used templates for preparing function which is independent
    // on the type, but this is not required
    template < typename T1, typename T2, typename T3, typename T4 > 
    void put_debug_info ( tri_logger_t & log, 
        T1 const & t1, T2 const & t2, T3 const & t3, T4 const & t4 )
    {
        if ( log.is_activated() )
        {
            *(log.ostream_ptr()) << t1 << " (" << t2 << ") : ";
            *(log.ostream_ptr()) << t3 << " = " << t4 << ::std::endl;
        } 
    }
    
    template < typename T > 
    void put_log_info ( tri_logger_t & log, 
        T const & t )
    {
        if ( log.is_activated() )
        {
            *(log.ostream_ptr()) << t << ::std::endl;
        } 
    }
} // namespace trivial_logger

// macro shows how to write macros which using user-defined functions
#define LOG_FN(name) \
    ::trivial_logger::put_debug_info ( trivial_logger::tri_logger(),\
    __FILE__, __LINE__, #name, (name) )

// below is a place for user defined logger formating data

// ...

/*#define LOG_INFO(name) \
    ::trivial_logger::put_log_info ( trivial_logger::tri_logger(), (name) )
    */

// macros for switching off and on logger
#define LOG_ON() \
    do { ::trivial_logger::tri_logger().activate ( true ); } while(false)
#define LOG_OFF() \
    do { ::trivial_logger::tri_logger().activate ( false ); } while(false)

#if defined(CLEANTLOG)
#undef LOG
#undef LOG_ON
#undef LOG_OFF
#undef LOG_FN
#undef LOG_STR
#undef LOG_INFO
#define LOG(name) do{}while(false)
#define LOG_FN(name) do{}while(false)
#define LOG_ON() do{}while(false)
#define LOG_OFF() do{}while(false)
#define LOG_STR(str) do{}while(false)
#define LOG_INFO(str) do{}while(false)
#endif

#endif // LOGGER_HPP_INCLUDED
