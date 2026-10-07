import PropTypes from 'prop-types'
import React from 'react'

// fullstack4j: a wicket (three stumps and two bails) on a disc in Apache Wicket's orange,
// next to the "fullstack4j start" wordmark.
function Logo({ className }) {
  return (
    <svg
      aria-hidden='true'
      focusable='false'
      data-icon='fullstack4j-start'
      role='img'
      xmlns='http://www.w3.org/2000/svg'
      x='0px'
      y='0px'
      className={className}
      viewBox='0 0 860 140'
    >
      <g>
        <circle className='st0' cx='70' cy='70' r='68' />
        <rect className='st1' x='45' y='50' width='10' height='58' rx='2' />
        <rect className='st1' x='65' y='50' width='10' height='58' rx='2' />
        <rect className='st1' x='85' y='50' width='10' height='58' rx='2' />
        <rect className='st1' x='45' y='39' width='23' height='6' rx='3' />
        <rect className='st1' x='72' y='39' width='23' height='6' rx='3' />
      </g>
      <text
        x='166'
        y='96'
        fill='currentColor'
        fontFamily='Metropolis, Arial, sans-serif'
        fontSize='76'
      >
        <tspan fontWeight='700'>fullstack4j</tspan>
        <tspan className='st0' fontWeight='400' dx='22'>
          start
        </tspan>
      </text>
    </svg>
  )
}

Logo.defaultProps = {
  className: '',
}

Logo.propTypes = {
  className: PropTypes.string,
}

export default Logo
